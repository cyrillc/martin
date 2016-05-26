package ch.zhaw.psit4.martin.timerplugin;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class TimerPlugin implements MartinPlugin{
    static IMartinContext context;
    private boolean active;

    @Override
    public void activate(IMartinContext context) throws Exception {
        TimerPlugin.context = context;     
        this.active = true;
    }

    @Override
    public void initializeRequest(String feature, long requestID) throws Exception {
        if(active) {
        	if(feature.equals("timerDuration")){
        		TimerDurationWork feature1 = new TimerDurationWork(requestID);
                context.registerWorkItem(feature1);
        	}
        	
        	if(feature.equals("timerTimestamp")){
        		TimerTimestampWork feature1 = new TimerTimestampWork(requestID);
                context.registerWorkItem(feature1);
        	}
            
        }
    }

    @Override
    public void deactivate() throws Exception {
        this.active = false;
    }


}
