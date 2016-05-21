package ch.zhaw.psit4.martin.language.analyis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.zhaw.psit4.martin.api.language.parts.ISentence;
import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.language.parts.Sentence;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
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

	private AnnotationPipeline annotationPipeline;
	private Annotation annotation;

	List<Phrase> phrasesPopState;
	boolean popStateDirty;
	List<SemanticGraph> semanticGraphs = new ArrayList<>();

	List<MOutput> predefinedAnswer = new ArrayList<>();

	public AnnotatedSentence() {
		super(null);
	}

	public AnnotatedSentence(String sentence, AnnotationPipeline annotationPipeline) {
		super(sentence);

		TIMING_LOG.logStart("Text analyzation");
		this.annotationPipeline = annotationPipeline;
		if (!"".equals(sentence)) {
			this.annotate();
			this.generatePhrasesAndSemanticGraph();
			this.resetPopState();
		}

		this.generadePredefinedAnswer();

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
			generateTimestampPhrases(sentence.get(TokensAnnotation.class));
			SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
			semanticGraphs.add(dependencies);
		}
	}

	public void generateSentencePhrases(List<CoreLabel> tokens) {
		StringBuilder sb = new StringBuilder();
		CoreLabel previousToken;
		CoreLabel currentToken = tokens.get(0);

		for (CoreLabel token : tokens) {
			previousToken = currentToken;
			currentToken = token;

			if (previousToken.get(NamedEntityTagAnnotation.class).equals(token.get(NamedEntityTagAnnotation.class))) {
				sb.append(" " + token.get(TextAnnotation.class));
			} else {
				Phrase phrase = new Phrase(sb.toString().trim());
				phrase.setNerTag(previousToken.get(NamedEntityTagAnnotation.class));
				phrase.setNormalizedValue(previousToken.get(NormalizedNamedEntityTagAnnotation.class));

				phrases.add(phrase);
				sb.setLength(0);
				sb.append(token.get(TextAnnotation.class));
			}
		}

		Phrase phrase = new Phrase(sb.toString().trim());
		phrase.setNerTag(currentToken.get(NamedEntityTagAnnotation.class));
		phrase.setNormalizedValue(currentToken.get(NormalizedNamedEntityTagAnnotation.class));
		phrases.add(phrase);

	}

	public void generateTimestampPhrases(List<CoreLabel> tokens) {
		String value = null;
		String normalizedValue = null;

		for (CoreLabel token : tokens) {
			if (isPartOfTimestamp(token)) {
				if (value == null) {
					value = token.get(TextAnnotation.class);
					normalizedValue = token.get(NormalizedNamedEntityTagAnnotation.class);
				} else {
					value += " " + token.get(TextAnnotation.class);

					Phrase phrase = new Phrase(value);
					phrase.setType(EBaseType.TIMESTAMP);
					phrase.setNormalizedValue(normalizedValue);
					phrases.add(phrase);
					value = null;
				}
			}
		}

		if (value != null) {
			Phrase phrase = new Phrase(value);
			phrase.setType(EBaseType.TIMESTAMP);
			phrase.setNormalizedValue(normalizedValue);
			phrases.add(phrase);
		}
	}

	public boolean isPartOfTimestamp(CoreLabel token) {
		return token.get(NamedEntityTagAnnotation.class).equals(EBaseType.DATE.getNerTag())
				|| token.get(NamedEntityTagAnnotation.class).equals(EBaseType.TIME.getNerTag());
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
			return new Phrase("");
		}
	}

	/**
	 * Generates predefined answers, that can be used for static stentences.
	 */
	public void generadePredefinedAnswer() {
		if ("".equalsIgnoreCase(text)) {
			predefinedAnswer.add(new MOutput(MOutputType.TEXT, "I can't hear you. Please speak louder."));
		}

		if ((this.getWords().contains("unit") && this.getWords().contains("tests"))
				|| this.getWords().contains("unittests")) {
			predefinedAnswer.add(new MOutput(MOutputType.HEADING, "Just be quiet!"));
			predefinedAnswer.add(new MOutput(MOutputType.TEXT, "I'm gonna getcha!"));
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE, "http://tclhost.com/gEFAjgp.gif"));

		}

		if ("test".equalsIgnoreCase(text)) {
			predefinedAnswer.add(new MOutput(MOutputType.HEADING, "Heading"));
			predefinedAnswer.add(new MOutput(MOutputType.TEXT,
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. \n\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. \n\nStet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."));
			predefinedAnswer.add(new MOutput(MOutputType.ERROR,
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua!"));
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE,
					"http://www.aviatorcameragear.com/wp-content/uploads/2012/07/placeholder_2.jpg"));
		}

		if (this.text.toLowerCase().startsWith("can you")) {
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE, "http://tclhost.com/YXRMgbt.gif"));
		}
	}

	public List<MOutput> getPredefinedAnswer() {
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
