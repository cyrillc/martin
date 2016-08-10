package ch.zhaw.psit4.martin.timerplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MDuration;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class TimerDurationWork extends Feature {

    public TimerDurationWork(long requestID) {
        super(requestID);
    }

    private MDuration duration;

    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception {
       duration = (MDuration) args.get("duration");
    }

    @Override
    public List<MOutput> execute() throws Exception {
    	List<MOutput> answer = new ArrayList<>();
    	
    
    	
    	Thread timerThread = new Thread(new TimerThread(duration.getMilliseconds(), TimerPlugin.context));
    	timerThread.start();
    	
    	
    	answer.add(new MOutput(MOutputType.TEXT, "OK, i set your timer to " + duration));
        
        return answer;
    }
}
