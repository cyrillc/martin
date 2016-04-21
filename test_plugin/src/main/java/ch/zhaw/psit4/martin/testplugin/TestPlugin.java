package ch.zhaw.psit4.martin.testplugin;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class TestPlugin implements MartinPlugin{

    public void init(IMartinContext context, String feature, long requestID) {
        TestPluginWork feature1 = new TestPluginWork(requestID);
        context.registerWorkItem(feature1);
    }

}
