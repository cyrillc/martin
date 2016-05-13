package ch.zhaw.psit4.martin.api.language.parts;

import java.util.List;

import ch.zhaw.psit4.martin.api.types.EBaseType;

public interface ISentence {
	public String getSentence();
	
	public List<Phrase> getPhrases();
	
	public List<Phrase> getPhrasesOfType(EBaseType baseType);
	
	public List<String> getWords();

}
