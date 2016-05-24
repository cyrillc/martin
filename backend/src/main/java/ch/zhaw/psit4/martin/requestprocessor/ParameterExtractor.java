package ch.zhaw.psit4.martin.requestprocessor;

import java.util.Collection;
import java.util.Set;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.MParameter;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.UniversalEnglishGrammaticalRelations;

public class ParameterExtractor {

    public static String extractParameter(MParameter parameter,
            AnnotatedSentence sentence, Collection<MKeyword> matchingKeywords) {

        String parameterAsString;

        switch (EBaseType.fromClassName(parameter.getType())) {

        case TIMESTAMP:
            parameterAsString = extractTimeStampParameter(sentence);
            break;

        case TEXT:
            parameterAsString = extractTextParameter(sentence,
                    matchingKeywords);
            break;

        default:
            parameterAsString = defaultExtraction(parameter, sentence);
        }

        return parameterAsString;
    }

    private static String extractTimeStampParameter(AnnotatedSentence sentence) {
        // Timestamp consists of Date and Time
        Phrase date = sentence.popPhraseOfType(EBaseType.DATE);
        Phrase time = sentence.popPhraseOfType(EBaseType.TIME);
        return (date.getValue() + " " + time.getValue()).trim();
    }

    /**
     * Build a String with the Nominal Nominal Modifiers of the keywords in the
     * sentence Example: the sentence "show me a picture of a dog in a hause"
     * returns: "dog hause"
     * 
     * @param sentence
     * @param matchingKeywords
     * @return
     */
    private static String extractTextParameter(AnnotatedSentence sentence,
            Collection<MKeyword> matchingKeywords) {

        String parameterAsString = "";

        // Working only with graph of first text sentence
        SemanticGraph dependencies = sentence.getSemanticGraphs().get(0);
        for (MKeyword keyword : matchingKeywords) {

            // Working only with first occurrence of the keyword
            IndexedWord indKeyWord = dependencies
                    .getAllNodesByWordPattern(keyword.getKeyword()).get(0);

            Set<IndexedWord> nominalMods = dependencies.getChildrenWithReln(
                    indKeyWord,
                    UniversalEnglishGrammaticalRelations.NOMINAL_MODIFIER);
            for (IndexedWord nominalModifier : nominalMods) {
                parameterAsString = parameterAsString
                        .concat(nominalModifier.value() + " ");
            }
        }

        return parameterAsString.trim();
    }

    private static String defaultExtraction(MParameter parameter,
            AnnotatedSentence sentence) {
        Phrase phrase = sentence
                .popPhraseOfType(EBaseType.fromClassName(parameter.getType()));
        return (phrase != null) ? phrase.getValue() : null;
    }
}
