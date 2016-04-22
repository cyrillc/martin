package ch.zhaw.psit4.martin.requestProcessor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

class Sentence {
	private ArrayList<Word> sentence = new ArrayList<>();
	private String rawSentence;
	
	private StanfordCoreNLP stanfordNLP;
	

	public Sentence(String sentence, StanfordCoreNLP stanfordNLP) {
		this.rawSentence = sentence;
		this.stanfordNLP = stanfordNLP;
		// Remove special characters
		sentence = sentence.replaceAll("[^a-zA-Z0-9- äöüÄÖÜ]", "");
		for (String sentencePart : sentence.split(" ")) {
			this.sentence.add(new Word(sentencePart));
		}
	}

	public ArrayList<Word> getWords() {
		return sentence;
	}
	
	public List<String> performNameEntityRecognition(String type){
		Annotation document = new Annotation(rawSentence);
		stanfordNLP.annotate(document);
		
		ArrayList<String> recognizedWords = new ArrayList<>();
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for(CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// Get all words of type
				if(token.get(NamedEntityTagAnnotation.class).equals(type)){
					recognizedWords.add(token.get(TextAnnotation.class));
				}
			}
		}
		
		return recognizedWords;
	}

	// http://corenlp.run/
	public void analyzeWithStanfordNLP() {
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(rawSentence);

		// run all Annotators on this text
		stanfordNLP.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			
		
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ner = token.get(NamedEntityTagAnnotation.class);
				System.out.println("word: " + word + " pos-tag: " + pos + " ner-label:" + ner);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);
			
			
			PrintWriter out = new PrintWriter(System.out);
			out.println("The first sentence parsed is:");
			tree.pennPrint(out);
					

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);

	}
}
