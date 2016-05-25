package ch.zhaw.psit4.martin.requestprocessor;

import java.util.Collection;
import java.util.List;
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

	public static Phrase extractParameter(MParameter parameter, AnnotatedSentence sentence,
			Collection<MKeyword> matchingKeywords) {
 
		Phrase parameterPhrase;

		switch (EBaseType.fromClassName(parameter.getType())) {
		case TEXT:
			parameterPhrase = extractTextParameter(sentence, matchingKeywords);
			break;
		default:
			parameterPhrase = defaultExtraction(parameter, sentence);
		}

		return parameterPhrase;
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
	public static Phrase extractTextParameter(AnnotatedSentence sentence, Collection<MKeyword> matchingKeywords) {

		String parameterAsString = "";

		// Working only with graph of first text sentence
		if (sentence.getSemanticGraphs().isEmpty() || sentence.getSemanticGraphs().get(0) == null) {
			return null;
		}
		SemanticGraph dependencies = sentence.getSemanticGraphs().get(0);
		for (MKeyword keyword : matchingKeywords) {

			// Working only with first occurrence of the keyword
			List<IndexedWord> indexKeywordList = dependencies.getAllNodesByWordPattern(keyword.getKeyword());
			if (indexKeywordList.isEmpty()) {
				return null;
			}
			IndexedWord indKeyWord = indexKeywordList.get(0);

			Set<IndexedWord> nominalMods = dependencies.getChildrenWithReln(indKeyWord,
					UniversalEnglishGrammaticalRelations.NOMINAL_MODIFIER);
			for (IndexedWord nominalModifier : nominalMods) {
				parameterAsString = parameterAsString.concat(nominalModifier.value() + " ");
			}
		}
		
		Phrase phrase = new Phrase(parameterAsString.trim());
		phrase.setType(EBaseType.TEXT);

		return phrase;
	}

	public static Phrase defaultExtraction(MParameter parameter, AnnotatedSentence sentence) {
		return sentence.popPhraseOfType(EBaseType.fromClassName(parameter.getType()));
	}
}
