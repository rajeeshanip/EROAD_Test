package com.eroad.rajeeshani.timezonetracker;

import static java.util.TimeZone.getTimeZone;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import com.eroad.rajeeshani.timezonetracker.service.TimeTrackerService;
import com.eroad.rajeeshani.timezonetracker.util.LatLngToTimezone;
import com.google.maps.model.LatLng;

/**
 * Rajeeshani
 */
public class TimeTrackerServiceTest {

	private LatLngToTimezone latLngToTimezoneConverter;
	private TimeTrackerService timeTrackerService;

	@Before
	public void setUp() {
		latLngToTimezoneConverter = mock(LatLngToTimezone.class);
		timeTrackerService = new TimeTrackerService();
	}

	@Test
	public void testConvert() {
		when(latLngToTimezoneConverter.apply(any(LatLng.class)))
				.thenReturn(Optional.of(getTimeZone(ZoneId.of("Pacific/Auckland"))));
		final List<String> row = new ArrayList<String>();
		row.add("2013-07-10 02:52:49");
		row.add("-44.490947");
		row.add("171.220966");

		final List<List<String>> input = new ArrayList<List<String>>();
		input.add(row);
		final List<String> actualOutput = timeTrackerService.convert(input, latLngToTimezoneConverter);
		String outputWIthoutNewLine = new String(actualOutput.toString());
		outputWIthoutNewLine = outputWIthoutNewLine.replaceAll("\n", "");
		assertThat(Lists.newArrayList(outputWIthoutNewLine)).isEqualTo(
				Lists.newArrayList("[2013-07-10 02:52:49,-44.490947,171.220966,Pacific/Auckland,2013-07-10 14:52:49]"));
	}
}
