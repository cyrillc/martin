package ch.zhaw.psit4.martin.requestProcessor;

class Word {
	public final String word;
	
	public Word(String word){
		this.word = word;
	}
	
	@Override
	public String toString(){
		return word;
	}
}
