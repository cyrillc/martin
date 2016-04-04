package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Text;

public class TestPluginWork extends Feature{

    public TestPluginWork(long requestID) {
        super(requestID);
    }

    private String name;
    
    @Override
    public void start(Map<String, IMartinType> args) throws Exception {
        Text text = (Text)args.get("name");
        name = text.getValue();
    }

    @Override
    public void run() throws Exception {
        // Do nothing
        
    }

    @Override
    public String stop() throws Exception {
        return "Hi " + name + "!";
    }

}
