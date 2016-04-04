package ch.zhaw.psit4.martin.common;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.util.Pair;

/**
* This class holds extended information about the request. It holds a possible 
* function call along with the required and optional arguments used to call 
* the plugin feature.
*
* @version 0.1
**/
public class ExtendedRequest {
	private Request input;
	private List<Call> calls;
	
	public ExtendedRequest() {
		this.calls = new ArrayList<Call>();
	}

	public Request getInput() {
		return this.input;
	}

	public void setInput(Request input) {
		this.input = input;
	}

	public void addCall(Call call){
		this.calls.add(call);
	}
	
	public List<Call> getCalls(){
		return this.calls;
	}
}
