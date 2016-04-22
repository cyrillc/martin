package ch.zhaw.psit4.martin.requestProcessor.utils;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NaturalLanguageAnalysisUtilities {
	
	public StanfordCoreNLP bootStanfordNLP(){
		
		Log logger = LogFactory.getLog(StanfordCoreNLP.class);
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		return new StanfordCoreNLP(props);
		
		// Dependencies:
		// http://stanfordnlp.github.io/CoreNLP/dependencies.html
	}

}
