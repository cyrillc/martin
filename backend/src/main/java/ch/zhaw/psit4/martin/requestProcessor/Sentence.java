package ch.zhaw.psit4.martin.requestProcessor;

import java.util.ArrayList;

import antlr.collections.List;

class Sentence {
	private ArrayList<Word> sentence = new ArrayList<>();
	
	public Sentence(String sentence){
		// Remove special characters
		sentence = sentence.replaceAll("[^a-zA-Z0-9- äöüÄÖÜ]", "");
		for(String sentencePart : sentence.split(" ")){
			this.sentence.add(new Word(sentencePart));
		}
	}
	
	public ArrayList<Word> getWords(){
		return sentence;
	}	
}
