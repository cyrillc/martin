package zhaw.weatherPlugin;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class WeatherPlugin implements MartinPlugin {

    public void init(IMartinContext context, String feature, long requestID) {
        
        Feature work;
        switch(feature.toLowerCase()){
            case "weather":
                work = new WeatherWork(requestID);
                break;
            case "forecast":
                work = new ForecastWork(requestID);
                break;
            default:
                // TODO what if no right feature is found?
                work = new WeatherWork(requestID);
        }
        context.registerWorkItem(work);
        
    }
}
