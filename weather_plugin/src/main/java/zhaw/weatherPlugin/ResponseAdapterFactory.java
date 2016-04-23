package zhaw.weatherPlugin;

import org.bitpipeline.lib.owm.WeatherStatusResponse;

/**
 * This is a factory for ResponseAdapters, can be user for different types of
 * responses by Weathers APIs. Could be because of multiple APIs are supported,
 * or because a single API return different responses based on the request 
 * (e.g. request for current weather o for the forecast)
 * 
 *
 */
public class ResponseAdapterFactory {

    public WeatherResponseAdapter getResponseAdapter(WeatherStatusResponse r) {
        return new WeatherResponseAdapter(r);
    }
}
