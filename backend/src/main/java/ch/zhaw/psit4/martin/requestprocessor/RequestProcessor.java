package ch.zhaw.psit4.martin.requestprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.language.typefactory.BaseTypeFactory;
import ch.zhaw.psit4.martin.models.MFunction;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.MParameter;
import ch.zhaw.psit4.martin.models.MPlugin;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;
import ch.zhaw.psit4.martin.models.repositories.MKeywordRepository;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.UniversalEnglishGrammaticalRelations;

/**
 * This class is responible for extending a request to a computer readable
 * format. It searches for possible function calls with their corresponding
 * arguments.
 *
 * @version 0.1
 **/
public class RequestProcessor {

    @Autowired
    private MKeywordRepository keywordRepository;

    @Autowired
    private AnnotationPipeline annotationPipeline;

    private static final Log LOG = LogFactory.getLog(RequestProcessor.class);
    private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory
            .getInstance();

    /**
     * Extends a request from a basic command and tries to determine possible
     * function calls.
     * 
     * @param request
     *            Raw request to be extended
     * @return Returns an ExtendedRequest with original-request and a possible
     *         executable function calls.
     */
    public ExtendedRequest extend(MRequest request, MResponse response) {
        TIMING_LOG.logStart(this.getClass().getSimpleName());

        ExtendedRequest extendedRequest = new ExtendedRequest(request,
                response);

        TIMING_LOG.logEnd(this.getClass().getSimpleName());
        AnnotatedSentence sentence = new AnnotatedSentence(
                extendedRequest.getRequest().getCommand(), annotationPipeline);
        TIMING_LOG.logStart(this.getClass().getSimpleName());

        // Find possible Calls by keywords
        List<PossibleCall> possibleCalls = getPossibleCallsWithKeywords(
                sentence.getWords());

        // Resolve parameters
        resolveParameters(possibleCalls, sentence);

        // Sort by relevance
        possibleCalls.sort((PossibleCall result1,
                PossibleCall result2) -> result1.getRelevance()
                        - result2.getRelevance());

        extendedRequest.setSentence(sentence);

        for (PossibleCall possibleCall : possibleCalls) {
            // Create Call
            Call call = new Call();
            call.setPlugin(possibleCall.getPlugin());
            call.setFunction(possibleCall.getFunction());
            call.setParameters(possibleCall.getParameters());

            extendedRequest.addCall(call);
        }

        TIMING_LOG.logEnd(this.getClass().getSimpleName());
        return extendedRequest;
    }

    /**
     * Searches the database for plugins/functions with the keywords provided
     * and adds them to the list.
     * 
     * @param possibleCalls
     *            List of possible results.
     * @param words
     *            words to be matched with the keywords.
     * @return the extended list
     */
    private List<PossibleCall> getPossibleCallsWithKeywords(
            List<String> words) {
        List<PossibleCall> possibleCalls = new ArrayList<>();

        for (String word : words) {

            MKeyword keyword = keywordRepository.findByKeywordIgnoreCase(word);

            if (keyword != null) {
                for (MFunction function : keyword.getFunctions()) {
                    MPlugin plugin = function.getPlugin();

                    Optional<PossibleCall> optionalPossibleResult = possibleCalls
                            .stream()
                            .filter(o -> o.getPlugin().getId() == plugin
                                    .getId())
                            .filter(o -> o.getFunction().getId() == function
                                    .getId())
                            .findFirst();

                    if (optionalPossibleResult.isPresent()) {
                        optionalPossibleResult.get()
                                .addMatchingKeyword(keyword);
                    } else {
                        PossibleCall possibleCall = new PossibleCall(plugin,
                                function);
                        possibleCall.addMatchingKeyword(keyword);
                        possibleCalls.add(possibleCall);
                    }
                }
            }

        }

        return possibleCalls;
    }

    /**
     * This function tries to determine the best word or phrase in the provided
     * sentence to fit into an argument that is needed from the function. It
     * uses the Stanford CoreNLP library to search for IMartinTypes in the
     * sentence.
     * 
     * The result of the parameter-filling may not be perfect and is heavy
     * dependent on the provided sentence. If the sentence has bad spelling or
     * is grammatically incorrect, the results may be not as good as expected.
     * The more details and contextual consistent the sentence is, the better
     * the results.
     * 
     * @param possibleCalls
     *            A list of possible results, whose parameters should be filled
     * @param sentence
     *            A sentence which provides the base for the parameter-finding
     * @return A list of PossibleResults with their corresponding parameters
     *         filled as good as possible
     */
    public List<PossibleCall> resolveParameters(
            List<PossibleCall> possibleCalls, AnnotatedSentence sentence) {
        for (PossibleCall possibleCall : possibleCalls) {
            MFunction function = possibleCall.getFunction();

            for (MParameter parameter : function.getParameters()) {
                // Create instance of IMartinType for requested type
                IBaseType parameterValue = getParameterValue(parameter,
                        sentence, possibleCall.getMatchingKeywords().values());
                possibleCall.addParameter(parameter.getName(), parameterValue);
            }
        }

        return possibleCalls;
    }

    public IBaseType getParameterValue(MParameter parameter,
            AnnotatedSentence sentence, Collection<MKeyword> matchingKeywords) {
        try {

            while (sentenceHasMoreParameterValues(sentence, parameter)) {

                String parameterAsString = "";

                if (isParameterTimeStamp(parameter)) {
                    parameterAsString = extractTimeStampParameter(sentence);
                } else if (isParameterText(parameter)) {
                    parameterAsString = extractTextParameter(sentence,
                            matchingKeywords);
                } else {
                    Phrase phrase = sentence.popPhraseOfType(
                            EBaseType.fromClassName(parameter.getType()));
                    if (phrase != null) {
                        parameterAsString = phrase.getValue();
                    }
                }

                if (parameterAsString.length() != 0) {
                    TIMING_LOG.logEnd(this.getClass().getSimpleName());
                    try {
                        IBaseType parameterValue = BaseTypeFactory.fromType(
                                EBaseType.fromClassName(parameter.getType()),
                                parameterAsString, sentence);
                        LOG.info(
                                "\n Parameter found via Name Entity Recognition: "
                                        + parameterValue.toString());
                        TIMING_LOG.logStart(this.getClass().getSimpleName());
                        return parameterValue;
                    } catch (BaseTypeInstanciationException e) {
                        TIMING_LOG.logStart(this.getClass().getSimpleName());
                        LOG.debug(e);
                    }
                }
            }

        } catch (Exception e) {
            LOG.debug(e);
            LOG.error("The IMartinType '" + parameter.getType()
                    + "' could not be found.");
        }
        return null;
    }

    private boolean isParameterTimeStamp(MParameter parameter) {
        return EBaseType.TIMESTAMP.equals(
                EBaseType.fromClassName(parameter.getType())) ? true : false;
    }

    private boolean isParameterText(MParameter parameter) {
        return EBaseType.TEXT.equals(
                EBaseType.fromClassName(parameter.getType())) ? true : false;
    }

    private boolean sentenceHasMoreParameterValues(AnnotatedSentence sentence,
            MParameter parameter) {

        Integer parametersLeft = 0;

        if (isParameterTimeStamp(parameter)) {
            parametersLeft = sentence.getPhrasesOfType(EBaseType.DATE).size()
                    + sentence.getPhrasesOfType(EBaseType.TIME).size();
        } else {
            parametersLeft = sentence
                    .getPhrasesOfType(
                            EBaseType.fromClassName(parameter.getType()))
                    .size();
        }

        return parametersLeft > 0 ? true : false;
    }

    private String extractTimeStampParameter(AnnotatedSentence sentence) {
        // Timestamp consists of Date and Time
        Phrase date = sentence.popPhraseOfType(EBaseType.DATE);
        Phrase time = sentence.popPhraseOfType(EBaseType.TIME);
        return (date.getValue() + " " + time.getValue()).trim();
    }

    /**
     * Build a String with the Nominal Nominal Modifiers of the keywords in the sentence
     * Example: the sentence "show me a picture of a dog in a hause" returns: "dog hause"
     * 
     * @param sentence
     * @param matchingKeywords
     * @return
     */
    private String extractTextParameter(AnnotatedSentence sentence,
            Collection<MKeyword> matchingKeywords) {
        
        String parameterAsString = "";
        
        // Working only with graph of first text sentence
        SemanticGraph dependencies = sentence.getSemanticGraphs().get(0);
        for (MKeyword keyword : matchingKeywords) {
            
            // Working only with first occurrence of the keyword
            IndexedWord indKeyWord = dependencies
                    .getAllNodesByWordPattern(keyword.getKeyword()).get(0);
                        
            Set<IndexedWord> nominalMods = dependencies.getChildrenWithReln(indKeyWord, 
                    UniversalEnglishGrammaticalRelations.NOMINAL_MODIFIER);
            for(IndexedWord nominalModifier : nominalMods){
                parameterAsString = parameterAsString.concat(nominalModifier.value() + " ");
            }
        }

        return parameterAsString.trim();
    }

}
