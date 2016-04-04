package ch.zhaw.psit4.martin.testplugin;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.PluginService;

public class TestPlugin implements PluginService{

    public void init(IMartinContext context) {
        TestPluginWork workItem = new TestPluginWork();
        context.registerWorkItem(workItem);
    }
}
