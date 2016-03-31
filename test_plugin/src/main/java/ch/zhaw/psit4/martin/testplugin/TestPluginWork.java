package ch.zhaw.psit4.martin.testplugin;

import java.util.Map;

import ch.zhaw.psit4.martin.api.WorkItem;
import ch.zhaw.psit4.martin.api.types.IMartinType;
import ch.zhaw.psit4.martin.api.types.Text;

public class TestPluginWork extends WorkItem{

    @Override
    public void onWorkStart(Map<String, IMartinType> args) throws Exception {
        Text text = (Text)args.get("name");
    }

    @Override
    public void doWork() throws Exception {
        // Do nothing
        
    }

    @Override
    public void onWorkDone() throws Exception {
        // Do nothing
    }

}
