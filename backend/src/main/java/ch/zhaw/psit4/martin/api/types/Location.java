package ch.zhaw.psit4.martin.api.types;


/**
 * A Location is a place or place or position. 
 * 
 * This can be:
 * - A geo-position on the surface of the earth
 * - ... ?
 * 
 * @author simonflepp
 *
 */
public class Location implements IMartinType {
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
