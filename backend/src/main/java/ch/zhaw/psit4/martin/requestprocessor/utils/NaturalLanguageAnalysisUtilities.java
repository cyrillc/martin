package ch.zhaw.psit4.martin.requestprocessor.utils;

import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

/**
 * This Class holds some Utilities for analyzing natural language to help
 * getting the best result for a user request.
 *
 */
public class NaturalLanguageAnalysisUtilities {
	public static final String CORENLP_SERVER = "corenlp.run";
	public static final Integer CORENLP_PORT = 80;
	public static final Integer CORENLP_MAX_THREADS = 1;
	

	/**
	 * Returns an Instance of StanfordCoreNLP. StanfordCoreNLP can be used to
	 * analyze natural language in an advanced way.
	 * 
	 * Source: http://stanfordnlp.github.io Try-Out: http://corenlp.run
	 * 
	 * @return A usable Stanford NLP pipline.
	 */
	public StanfordCoreNLP bootStanfordNLP() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		
		return new StanfordCoreNLP(props);
	}
	
	/**
	 * Returns an Instance of StanfordCoreNLPClient. StanfordCoreNLP can be used to
	 * analyze natural language in an advanced way.
	 * 
	 * The client is a leightweight version of CoreNLP, which connects to corenlp.run for 
	 * fetching analyzing text.
	 * 
	 * Source: http://stanfordnlp.github.io Try-Out: http://corenlp.run
	 * 
	 * @return A usable Stanford NLP pipline.
	 */
	public StanfordCoreNLPClient bootStanfordNLPClient() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
	
		return new StanfordCoreNLPClient(props, CORENLP_SERVER, CORENLP_PORT, CORENLP_MAX_THREADS);
	}

}
