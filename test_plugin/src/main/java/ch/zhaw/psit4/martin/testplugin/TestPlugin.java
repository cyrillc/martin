package ch.zhaw.psit4.martin.testplugin;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class TestPlugin implements MartinPlugin{
    static IMartinContext context;
    private boolean active;

    @Override
    public void activate(IMartinContext context) throws Exception {
        TestPlugin.context = context;     
        this.active = true;
    }

    @Override
    public void initializeRequest(String feature, long requestID) throws Exception {
        if(active) {
            TestPluginWork feature1 = new TestPluginWork(requestID);
            context.registerWorkItem(feature1);
        }
    }

    @Override
    public void deactivate() throws Exception {
        this.active = false;
    }


}
