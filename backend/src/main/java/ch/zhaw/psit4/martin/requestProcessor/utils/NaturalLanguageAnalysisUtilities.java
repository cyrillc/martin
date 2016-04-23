package ch.zhaw.psit4.martin.requestprocessor.utils;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * This Class holds some Utilities for analyzing natural language to help
 * getting the best result for a user request.
 *
 */
public class NaturalLanguageAnalysisUtilities {

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

}
