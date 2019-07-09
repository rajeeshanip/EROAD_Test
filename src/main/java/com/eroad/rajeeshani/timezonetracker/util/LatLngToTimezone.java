package com.eroad.rajeeshani.timezonetracker.util;

import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.eroad.rajeeshani.timezonetracker.exception.ServiceException;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.LatLng;

/**
 * Rajeeshani
 */
public class LatLngToTimezone implements Function<LatLng, Optional<TimeZone>> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	private static final String API_KEY = "";
	private static GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);

	@Override
	public Optional<TimeZone> apply(LatLng latLng) {
		Optional<TimeZone> timezone = Optional.empty();
		try {
			timezone = Optional.of(TimeZoneApi.getTimeZone(context, latLng).await());
		} catch (Exception e) {
			logger.error("LatLngToTimezone service call error", e);
			throw new ServiceException(ErrorCodes.UNKNOWN_ERROR,
					messageSource.getMessage(String.valueOf(ErrorCodes.UNKNOWN_ERROR), null, null));
		}
		return timezone;
	}
}
