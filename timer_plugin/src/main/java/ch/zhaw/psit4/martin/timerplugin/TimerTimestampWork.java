package ch.zhaw.psit4.martin.timerplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class TimerTimestampWork extends Feature {

    public TimerTimestampWork(long requestID) {
        super(requestID);
    }

    private MTimestamp time;

    @Override
    public void initialize(Map<String, IBaseType> args) throws Exception {
       time = (MTimestamp) args.get("time");
    }

    @Override
    public List<MOutput> execute() throws Exception {
    	List<MOutput> answer = new ArrayList<>();
    	
    	answer.add(new MOutput(MOutputType.TEXT, time.toString()));
        
        return answer;
    }
}
