package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.PluginService;
import ch.zhaw.psit4.martin.api.types.IMartinType;

public class TestPlugin implements PluginService{

    public void init(final Map<String, IMartinType> map) {
        System.out.println("Plugin initialized");
    }

    public void contextualize(final IMartinContext context) {
        System.out.println("Plugin contextualized!");
    }

}
