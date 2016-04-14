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
    
    private String value;
    
    public Text() {
        this.value = "null";
    }
    public Text(String value) {
        this.value = value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see ch.zhaw.psit4.martin.api.types.IMartinType#fromString(java.lang.String)
     */
    public void fromString(String data) {
        this.value = (new JSONObject(data)).getString("value");
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return (new JSONObject()).put("value", this.value).toString();
    }

}
