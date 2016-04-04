package ch.zhaw.psit4.martin.common;

/**
* This class holds extended information about the request. It holds a possible 
* function call along with the required and optional arguments used to call 
* the plugin feature.
*
* @version 0.1
**/
public class ExtendedRequest {
	private Request input;
	private Call call;
	
	public ExtendedRequest() {
	}

	public Request getInput() {
		return this.input;
	}

	public void setInput(Request input) {
		this.input = input;
	}

	public void setCall(Call call){
		this.call = call;
	}
	
	public Call getCall(){
		return this.call;
	}
}
