package ch.zhaw.psit4.martin.api.typefactory;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import ch.zhaw.psit4.martin.api.types.IMartinTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.Location;
import ch.zhaw.psit4.martin.boot.MartinBoot;

public class LocationFactory {
	private static final String GOOGLE_GEOLOCATION_API_KEY = "AIzaSyAqUfeSyNLB7YTslza6EqAZ9cHjSECg14U";

	private static final Log LOG = LogFactory.getLog(MartinBoot.class);

	public Location fromString(String rawInput) throws IMartinTypeInstanciationException {
		Location location = new Location(rawInput);

		GeocodingResult[] results = getGeolocationFromGoogle(rawInput);

		if (results.length > 0) {
			location.setFormattedAddress(Optional.ofNullable(results[0].formattedAddress));
			location.setLatitude(Optional.ofNullable(results[0].geometry.location.lat));
			location.setLongitude(Optional.ofNullable(results[0].geometry.location.lng));
		}

		return location;
	}

	private GeocodingResult[] getGeolocationFromGoogle(String rawLocation) {
		GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_GEOLOCATION_API_KEY);
		try {
			return GeocodingApi.geocode(context, rawLocation).await();
		} catch (Exception e) {
			LOG.error(e);
		}

		return new GeocodingResult[0];
	}
}