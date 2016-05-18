package zhaw.weatherPlugin;

import ch.zhaw.psit4.martin.api.Feature;
import ch.zhaw.psit4.martin.api.IMartinContext;
import ch.zhaw.psit4.martin.api.MartinPlugin;

public class WeatherPlugin implements MartinPlugin {
    
    private IMartinContext context;
    private boolean active;

    @Override
    public void activate(IMartinContext context) throws Exception {
        this.context = context;     
        this.active = true;
    }

    @Override
    public void initializeRequest(String feature, long requestID) throws Exception {
        if(active) {
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

    @Override
    public void deactivate() throws Exception {
        this.active = false;
    }
}
