package ch.zhaw.psit4.martin.timerplugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class TimerThread implements Runnable {
	private IMartinContext context;
	private Long waitTimeInMilliseconds;
	private static final Log LOG = LogFactory.getLog(TimerThread.class);
	
	public TimerThread(Long waitTimeInMilliseconds, IMartinContext context){
		this.waitTimeInMilliseconds = waitTimeInMilliseconds;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(waitTimeInMilliseconds);
			
			List<MOutput> outputs = new ArrayList<>();
			outputs.add(new MOutput(MOutputType.TEXT, "Peep, peeep, peeeeeeeeeeep!"));
			outputs.add(new MOutput(MOutputType.AUDIO, "http://www.orangefreesounds.com/wp-content/uploads/2015/04/Cuckoo-bird-sound.mp3"));
		
			context.addToOutputQueue(outputs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
