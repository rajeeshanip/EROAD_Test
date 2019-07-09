/**
 * Contains the business logic off the project
 */
package com.eroad.rajeeshani.timezonetracker.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eroad.rajeeshani.timezonetracker.exception.ServiceException;
import com.eroad.rajeeshani.timezonetracker.util.ErrorCodes;
import com.eroad.rajeeshani.timezonetracker.util.LatLngToTimezone;
import com.google.common.net.HttpHeaders;
import com.google.maps.model.LatLng;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * @author Rajeeshani
 *
 */

@Service
public class TimeTrackerService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Value("${file.upload.dir}")
	private String filePath;

	public void processFile(MultipartFile file, HttpServletResponse response) {
		List<List<String>> records = new ArrayList<List<String>>();

		try {
			Reader reader = new InputStreamReader(file.getInputStream());
			CSVReader csvReader = new CSVReaderBuilder(reader).build();
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				records.add(Arrays.asList(values));
			}
			logger.info("Successfully read the file. records : {}", records);
			Function<LatLng, Optional<TimeZone>> latLngToTimezone = new LatLngToTimezone();
			List<String> output = convert(records, latLngToTimezone);
			logger.info("TimeZone convertion done successfully. output : {}", output);

			generateNewCSV(output, response);
		} catch (IOException e) {
			logger.error("File Processing error", e);
			throw new ServiceException(ErrorCodes.UNKNOWN_ERROR,
					messageSource.getMessage(String.valueOf(ErrorCodes.UNKNOWN_ERROR), null, null));
		}

	}

	public List<String> convert(List<List<String>> records, Function<LatLng, Optional<TimeZone>> latLngToTimezone) {

		return records.stream().map(input -> {
			String dateTime = input.get(0);
			Double lat = Double.valueOf(input.get(1));
			Double lng = Double.valueOf(input.get(2));
			LatLng latLng = new LatLng(lat, lng);
			Optional<TimeZone> timeZone = latLngToTimezone.apply(latLng);
			if (!timeZone.isPresent()) {
				logger.error("Invalid latlng input{}", latLng);
				throw new ServiceException(ErrorCodes.INVALID_INPUT,
						messageSource.getMessage(String.valueOf(ErrorCodes.INVALID_INPUT), null, null));
			}
			logger.info("Converted timezone : {} for latlng {}", timeZone.get().getID(), latLng);
			LocalDateTime localDateTime = getLocalDateFromUtc(dateTime);
			String formattedLocalDateTime = localDateTime.toString().replace("T", " ");
			logger.info("formatted locale time: {} for date datetime : {}", formattedLocalDateTime, dateTime);
			String output = dateTime.concat(",").concat(Double.toString(lat)).concat(",").concat(Double.toString(lng))
					.concat(",").concat(timeZone.get().getID()).concat(",").concat(formattedLocalDateTime).concat("\n");
			logger.info("Output string : {}", output);
			return output;
		}).collect(Collectors.toList());
	}

	private DateTimeFormatter getUTCDateTimeFormat() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
	}

	private LocalDateTime getLocalDateFromUtc(String dateTime) {
		final DateTimeFormatter UTCTimeFormat = getUTCDateTimeFormat();
		final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, UTCTimeFormat);
		return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("Pacific/Auckland"));
	}

	private void generateNewCSV(List<String> output, HttpServletResponse response) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		// set file name and content type
		String filename = "output_" + dateFormat.format(date) + ".csv";

		String csvFilePath = filePath + filename;

		try {
			FileWriter csvWriter = new FileWriter(csvFilePath);
			for (String row : output) {
				csvWriter.append(row);
			}
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			logger.error("File Processing error", e);
			throw new ServiceException(ErrorCodes.UNKNOWN_ERROR,
					messageSource.getMessage(String.valueOf(ErrorCodes.UNKNOWN_ERROR), null, null));
		}

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

	}

}
