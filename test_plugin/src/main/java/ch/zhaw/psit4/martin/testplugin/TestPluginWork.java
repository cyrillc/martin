package ch.zhaw.psit4.martin.testplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MPerson;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class TestPluginWork extends Feature {

    public TestPluginWork(long requestID) {
        super(requestID);
    }

    private MPerson person1;
    private MPerson person2;
    private static String MY_NAME = "Martin";

    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception {
        person1 = (MPerson) args.get("name1");

        if (args.get("name2") != null) {
            person2 = (MPerson) args.get("name2");
        }
    }

    @Override
    public List<MOutput> execute() throws Exception {
        testOutputQueue();
        
        
        List<MOutput> ret = new ArrayList<>();
        if (person1 == null || person2 == null) {
            if (person1 != null && !person1.toString().equalsIgnoreCase(MY_NAME)) {
                ret.add(createOutputText("Who is " + person1 + "?"));
            }
            else if (person2 != null && !person2.toString().equalsIgnoreCase(MY_NAME)) {
                ret.add(createOutputText("Who is " + person2 + "?"));
            } else {
                ret.add(createOutputText("Hello my friend!"));
            }
        } else if (person1.toString().equalsIgnoreCase(MY_NAME)) {
            ret.add(createOutputText("Hello " + person2 + ", it's me " + person1 + "!"));
        } else if (person2.toString().equalsIgnoreCase(MY_NAME)) {
            ret.add(createOutputText("Hello " + person1 + ", it's me " + person2 + "!"));
        } else {
            ret.add(createOutputText("Hello " + person1 + " and " + person2 + "!"));
        }
        return ret;
    }

    private void testOutputQueue() {
        List<MOutput> ret1 = new ArrayList<>();
        List<MOutput> ret2 = new ArrayList<>();
        List<MOutput> ret3 = new ArrayList<>();
        
        ret1.add(createOutputText("Hello 1"));
        ret1.add(createOutputText("Hello 2"));
        ret2.add(createOutputText("Hello 3"));
        ret3.add(createOutputText("Hello 4"));
        ret1.add(createOutputText("Hello 5"));

        TestPlugin.context.addToOutputQueue(ret1);
        TestPlugin.context.addToOutputQueue(ret2);
        TestPlugin.context.addToOutputQueue(ret3);
    }

    private MOutput createOutputText(String text) {
        return new MOutput(MOutputType.TEXT, text);
    }
}
