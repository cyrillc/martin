package ch.zhaw.psit4.martin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class represents a sequence of words capable of standing alone to make
 * an assertion, ask a question, or give a command, usually consisting of a
 * subject and a predicate containing a finite verb
 *
 */
public class Sentence {
	private String rawSentence;

	private StanfordCoreNLP textAnalyzer;

	List<Phrase> phrases = new ArrayList<>();

	public Sentence(String sentence, StanfordCoreNLP textAnalyzer) {
		this.rawSentence = sentence;
		this.textAnalyzer = textAnalyzer;
		this.generateNamedEntityRecognitionTokens();
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
	private void generateNamedEntityRecognitionTokens() {
		Annotation document = new Annotation(rawSentence);
		textAnalyzer.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		StringBuilder sb = new StringBuilder();

		for (CoreMap sentence : sentences) {

			String previousNerToken = "O";
			String currentNerToken = "O";
			boolean newToken = true;
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				currentNerToken = token.get(NamedEntityTagAnnotation.class);
				String word = token.get(TextAnnotation.class);

				if (currentNerToken.equals("O")) {
					if (!previousNerToken.equals("O") && (sb.length() > 0)) {
						phrases.add(new Phrase(previousNerToken, sb.toString()));
						sb.setLength(0);
						newToken = true;
					}
					continue;
				}

				if (newToken) {
					previousNerToken = currentNerToken;
					newToken = false;
					sb.append(word);
					continue;
				}

				if (currentNerToken.equals(previousNerToken)) {
					sb.append(" " + word);
				} else {
					phrases.add(new Phrase(previousNerToken, sb.toString()));
					sb.setLength(0);
					newToken = true;
				}
				previousNerToken = currentNerToken;
			}
		}
	}

	/**
	 * Removes one element of type (LOCATION, PERSON, ORGANIZATION, ...) from
	 * the internal list and returns it.
	 * 
	 * @param type
	 *            Type (PERSON, LOCATION, ORGANIZATION, MISC, MONEY, NUMBER,
	 *            ORDINAL, PERCENT, DATE, TIME, DURATION, SET)
	 * @return a phrese with the chosen type
	 */
	public Phrase popPhraseOfType(String type) {
		Optional<Phrase> token = phrases.stream().filter(o -> o.getType().equals(type)).findFirst();

		if (token.isPresent()) {
			phrases.remove(phrases.indexOf(token.get()));
			return token.get();
		} else {
			return null;
		}
	}

	/**
	 * Removes one element of type IMartinType from the internal list and
	 * returns it.
	 * 
	 * @param type full IMartinType classname as String (with package)
	 * @return a phrese with the chosen type
	 */
	public Phrase popPhraseOfIMartinType(String iMartinType) {
		Optional<Phrase> token = phrases.stream().filter(o -> o.getIMartinType().equals(iMartinType)).findFirst();

		if (token.isPresent()) {
			phrases.remove(phrases.indexOf(token.get()));
			return token.get();
		} else {
			return new Phrase("", "");
		}
	}

	/**
	 * Gets all phrases with a chosen IMartionType
	 * @param iMartinType full IMartinType classname as String (with package)
	 * @return a list of chosen phrases
	 */
	public List<Phrase> getPhrasesOfIMartionType(String iMartinType) {
		return phrases.stream().filter(o -> o.getIMartinType().equals(iMartinType))
				.collect(Collectors.<Phrase> toList());
	}

	public List<Phrase> getPhrases() {
		return phrases;
	}

	public String getRawSentence() {
		return rawSentence;
	}

	public String[] getWords() {
		return rawSentence.replaceAll("[^a-zA-Z0-9- äöüÄÖÜ]", "").split(" ");
	}
}
