package ch.zhaw.psit4.martin.api.types;

/**
 * Distributed Text type for MArtIn API.
 * 
 * This is a String wrapper for the MArtIn API.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class Text implements IMartinType{
	private boolean isValid = false;
	private String data;

	@Override
	public void fromString(String data) throws IMartinTypeInstanciationException {
		this.data = data;
		this.isValid = true;
	}

	@Override
	public boolean isInstancaeableWith(String data) {
		return true;
	}
	
	@Override
	public String toString(){
		return data;
	}

	@Override
	public boolean isValid(){
		return isValid;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void fromJSON(String json) {
		// TODO Auto-generated method stub
		
	}
}
