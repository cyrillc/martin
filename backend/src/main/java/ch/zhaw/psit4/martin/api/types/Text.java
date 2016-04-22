package ch.zhaw.psit4.martin.api.types;

import org.json.JSONObject;

/**
 * Distributed Text type for MArtIn API.
 * 
 * This is a String wrapper for the MArtIn API.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class Text implements IMartinType{
	private boolean instance = false;
    
    private String value;
    
    /* (non-Javadoc)
     * @see ch.zhaw.psit4.martin.api.types.IMartinType#fromString(java.lang.String)
     */
    public void fromString(String data) throws MartinTypeInstanciationException{
        this.value = data;
        this.instance = true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return value;
    }
	@Override
	public boolean isInstancaeableWith(String data) {
		return true;
	}
	
	@Override
	public boolean isInstance() {
		return instance;
	}

}
