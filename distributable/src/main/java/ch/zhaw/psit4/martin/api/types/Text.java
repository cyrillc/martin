package ch.zhaw.psit4.martin.api.types;

import org.json.JSONObject;

public class Text implements IMartinType {
    
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
    
    public void fromString(String data) {
        this.value = (new JSONObject(data)).getString("value");
    }
    
    public String toString() {
        return (new JSONObject()).put("value", this.value).toString();
    }
}
