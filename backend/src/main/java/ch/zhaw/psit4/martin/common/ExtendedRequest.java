package ch.zhaw.psit4.martin.common;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import ch.zhaw.psit4.martin.models.MRequest;

/**
 * This class holds extended information about the request. It holds a possible
 * function call along with the required and optional arguments used to call the
 * plugin feature.
 *
 * @version 0.1
 **/
public class ExtendedRequest {
	/**
	 * The unique ID of this item. The id is distributed by the {@link UUID}
	 * implementation.
	 */
	private UUID id;
	/**
	 * The raw Request containing a command string.
	 */
	private MRequest input;
	/**
	 * List of possible Calls for the request ordered by possibility.
	 */
	private List<Call> calls;
	
	/**
	 * Parsed and analyzed sentence for further analysis.
	 */
	private Sentence sentence;
	

	public ExtendedRequest() {
		this.calls = new ArrayList<Call>();
		this.id = UUID.randomUUID();
	}

	public UUID getID() {
		return this.id;
	}

	public void setID(UUID id) {
		this.id = id;
	}

	public MRequest getInput() {
		return this.input;
	}

	public void setInput(MRequest input) {
		this.input = input;
	}

	public void addCall(Call call) {
		this.calls.add(call);
	}

	public List<Call> getCalls() {
		return this.calls;
	}
	
	public Sentence getSentence(){
		return this.sentence;
	}
	
	public void setSentence(Sentence sentence){
		this.sentence = sentence;
	}
}
