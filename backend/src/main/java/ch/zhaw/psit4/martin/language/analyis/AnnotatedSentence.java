package ch.zhaw.psit4.martin.language.analyis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.zhaw.psit4.martin.api.language.parts.ISentence;
import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.language.parts.Sentence;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class represents a sequence of words capable of standing alone to make
 * an assertion, ask a question, or give a command, usually consisting of a
 * subject and a predicate containing a finite verb
 *
 */

public class AnnotatedSentence extends Sentence implements ISentence {
	private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory.getInstance();
	private static final String UNKNOWN_NER_TAG = "O";

	public AnnotationPipeline annotationPipeline;
	private Annotation annotation;

	List<Phrase> phrasesPopState;
	boolean popStateDirty;
	List<SemanticGraph> semanticGraphs = new ArrayList<>();

	String predefinedAnswer;

	public AnnotatedSentence() {
		super(null);
	}

	public AnnotatedSentence(String sentence, AnnotationPipeline annotationPipeline) {
		super(sentence);

		TIMING_LOG.logStart("Text analyzation");
		this.annotationPipeline = annotationPipeline;
		this.annotate();
		this.generatePhrasesAndSemanticGraph();
		this.generadePredefinedAnswer();
		this.resetPopState();
		TIMING_LOG.logEnd("Text analyzation");
	}

	public void annotate() {
		annotation = new Annotation(text);
		annotationPipeline.annotate(annotation);
	}

	/**
	 * Recognizes named (PERSON, LOCATION, ORGANIZATION, MISC), numerical
	 * (MONEY, NUMBER, ORDINAL, PERCENT), and temporal (DATE, TIME, DURATION,
	 * SET) entities. Named entities are recognized using a combination of three
	 * CRF sequence taggers trained on various corpora, such as ACE and MUC.
	 * Numerical entities are recognized using a rule-based system. Numerical
	 * entities that require normalization, e.g., dates, are normalized to
	 * NormalizedNamedEntityTagAnnotation.
	 */
	public void generatePhrasesAndSemanticGraph() {
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {

			generateSentencePhrases(sentence.get(TokensAnnotation.class));
			SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
			semanticGraphs.add(dependencies);

		}

	}

	public void generateSentencePhrases(List<CoreLabel> tokens) {

		StringBuilder sb = new StringBuilder();

		String previousNerToken = UNKNOWN_NER_TAG;
		String currentNerToken = UNKNOWN_NER_TAG;

		for (CoreLabel token : tokens) {
			previousNerToken = currentNerToken;
			currentNerToken = token.get(NamedEntityTagAnnotation.class);
			String word = token.get(TextAnnotation.class);

			if (previousNerToken.equals(currentNerToken)) {
				sb.append(" " + word);
			} else {
				phrases.add(new Phrase(previousNerToken, sb.toString()));
				sb.setLength(0);
				sb.append(word);
			}
		}
		phrases.add(new Phrase(currentNerToken, sb.toString()));
	}

	public void resetPopState() {
		phrasesPopState = new ArrayList<>(phrases);
		popStateDirty = false;
	}

	public boolean isPopStateDirty() {
		return popStateDirty;
	}

	/**
	 * Removes one element of type IMartinType from the internal list and
	 * returns it.
	 * 
	 * @param type
	 *            full IMartinType classname as String (with package)
	 * @return a phrese with the chosen type
	 */
	public Phrase popPhraseOfType(EBaseType type) {
		popStateDirty = true;

		Optional<Phrase> token = phrasesPopState.stream().filter(o -> o.getType().equals(type)).findFirst();

		if (token.isPresent()) {
			phrasesPopState.remove(phrasesPopState.indexOf(token.get()));
			return token.get();
		} else {
			return new Phrase("", "");
		}
	}

	/**
	 * Generates predefined answers, that can be used for static stentences.
	 */
	public void generadePredefinedAnswer() {
		if ("".equalsIgnoreCase(text)) {
			predefinedAnswer = "I can't hear you. Please speak louder.";
		}

		if ((this.getWords().contains("unit") && this.getWords().contains("tests"))
				|| this.getWords().contains("unittests")) {
			predefinedAnswer = "<img src='http://tclhost.com/gEFAjgp.gif' />";
		}

		if (this.text.toLowerCase().startsWith("can you")) {
			predefinedAnswer = "<img src='http://tclhost.com/YXRMgbt.gif'>";
		}
	}

	public String getPredefinedAnswer() {
		return predefinedAnswer;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public List<SemanticGraph> getSemanticGraphs() {
		return semanticGraphs;
	}

	public void setSemanticGraphs(List<SemanticGraph> semanticGraphs) {
		this.semanticGraphs = semanticGraphs;
	}

}
