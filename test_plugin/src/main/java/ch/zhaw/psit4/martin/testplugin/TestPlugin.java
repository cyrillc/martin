package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.MartinContext;
import ch.zhaw.psit4.martin.api.PluginService;

public class TestPlugin implements PluginService{

    public void init(final Map<String, String> map) {
        System.out.println("Plugin initialized");
    }

    public void contextualize(final MartinContext context) {
        System.out.println("Plugin contextualized!");
    }

}
