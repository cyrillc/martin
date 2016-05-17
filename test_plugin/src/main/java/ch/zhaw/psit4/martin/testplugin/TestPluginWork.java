package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MPerson;

public class TestPluginWork extends Feature{

    public TestPluginWork(long requestID) {
        super(requestID);
    }

    private MPerson person1;
    private MPerson person2;
    private static String MY_NAME = "Martin";
    
    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception {
        person1 = (MPerson)args.get("name1");
        
        if(args.get("name2") != null){
        	person2 = (MPerson)args.get("name2");
        }
    }

    @Override
    public String execute() throws Exception {
    	if(person1 == null || person2 == null){
    		if(person1 != null && !person1.toString().equalsIgnoreCase(MY_NAME)){
    			return "Who is " + person1.toString() + "?";
    		}
    		if(person2 != null && !person2.toString().equalsIgnoreCase(MY_NAME)){
    			return "Who is " + person2.toString() + "?";
    		}
    		return "Hello my friend!";
    	} else if(person1.toString().equalsIgnoreCase(MY_NAME)){
    		return "Hello " + person2.toString() + ", it's me " + person1.toString() + "!";
    	} else if(person2.toString().equalsIgnoreCase(MY_NAME)){
    		return "Hello " + person1.toString() + ", it's me " + person2.toString() + "!";
    	} else {
    		return "Hello " + person1.toString() + " and " + person2.toString() + "!";
    	}
    }
}
