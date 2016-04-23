package ch.zhaw.psit4.martin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Sentence {
	private String rawSentence;

	private StanfordCoreNLP textAnalyzer;

	List<Phrase> expressions = new ArrayList<>();

	public Sentence(String sentence, StanfordCoreNLP textAnalyzer) {
		this.rawSentence = sentence;
		this.textAnalyzer = textAnalyzer;
		this.generateNamedEntityRecognitionTokens();
	}

	public List<String> performNameEntityRecognition(String type) {
		Annotation document = new Annotation(rawSentence);
		textAnalyzer.annotate(document);

		ArrayList<String> recognizedWords = new ArrayList<>();

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// Get all words of type
				if (token.get(NamedEntityTagAnnotation.class).equals(type)) {
					recognizedWords.add(token.get(TextAnnotation.class));
				}
			}
		}

		return recognizedWords;
	}

	private void generateNamedEntityRecognitionTokens() {
		Annotation document = new Annotation(rawSentence);
		textAnalyzer.annotate(document);
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		StringBuilder sb = new StringBuilder();

		for (CoreMap sentence : sentences) {
			
			String previousNerToken = "O";
			String currentNerToken = "O";
			boolean newToken = true;
			for(CoreLabel token : sentence.get(TokensAnnotation.class)) {
				currentNerToken = token.get(NamedEntityTagAnnotation.class);
				String word = token.get(TextAnnotation.class);
			
				if (currentNerToken.equals("O")) {
					if(!previousNerToken.equals("O") && (sb.length() > 0)) {
						expressions.add(new Phrase(previousNerToken, sb.toString()));
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
					expressions.add(new Phrase(previousNerToken, sb.toString()));
					sb.setLength(0);
					newToken = true;
				}
				previousNerToken = currentNerToken;
			}
		}
	}
	
	public Phrase popExpressionOfType(String type){
		Optional<Phrase> token = expressions.stream().filter(o -> o.getType().equals(type)).findFirst();
		
		if(token.isPresent()){
			expressions.remove(expressions.indexOf(token.get()));
			return token.get();
		} else {
			return null;
		}
	}
	
	public Phrase popExpressionOfIMartinType(String iMartinType){
		Optional<Phrase> token = expressions.stream().filter(o -> o.getIMartinType().equals(iMartinType)).findFirst();
		
		if(token.isPresent()){
			expressions.remove(expressions.indexOf(token.get()));
			return token.get();
		} else {
			return new Phrase("O", "");
		}
	}
	
	public List<Phrase> getExpressions(){
		return expressions;
	}
	
	public String getRawSentence() {
		return rawSentence;
	}
	
	public String[] getWords(){
		return rawSentence.replaceAll("[^a-zA-Z0-9- äöüÄÖÜ]", "").split(" ");
	}
}
