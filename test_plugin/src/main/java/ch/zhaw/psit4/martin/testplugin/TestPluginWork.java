package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MPerson;

public class TestPluginWork extends Feature{

    public TestPluginWork(long requestID) {
        super(requestID);
    }

    private String name1;
    private String name2;
    
    @Override
    public void start(Map<String, IBaseType> args) throws Exception {
        MPerson person1 = (MPerson)args.get("name1");
        MPerson person2 = (MPerson)args.get("name2");
        name1 = person1.toString();
        name2 = person2.toString();
    }

    @Override
    public void run() throws Exception {
        // Do nothing
        
    }

    @Override
    public String stop() throws Exception {
        return "Hi " + name2 + ", it's me " + name1;
    }

}
