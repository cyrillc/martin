package zhaw.weatherPlugin;

import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class WeatherPlugin implements MartinPlugin {

    public void init(IMartinContext context, String feature, long requestID) {
        WeatherWork work = new WeatherWork(requestID);
        context.registerWorkItem(work);
    }
}
