/**
 * Rest controller to consume request
 */
package com.eroad.rajeeshani.timezonetracker.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eroad.rajeeshani.timezonetracker.exception.ServiceException;
import com.eroad.rajeeshani.timezonetracker.service.TimeTrackerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Rajeeshani
 *
 */
@RestController
@CrossOrigin
public class TimeZoneTrackerController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TimeTrackerService timeTrackerService;

	/**
	 * Accept a CSV with a UTC datetime, latitude and longitude columns and append
	 * the timezone the vehicle is in and the localised datetime
	 *
	 * @return Modified CSV file
	 */
	@ApiOperation(value = "Submit input CSV file", notes = "Submit input CSV file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfuly generated the new CSV file"),
			@ApiResponse(code = 406, message = "Fail to generated the new CSV file") })
	@PostMapping(value = "/timeZone", produces = "text/csv")
	public void getTimeZone(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		try {
			timeTrackerService.processFile(file,response);
			logger.info("TimeZone tracker CSV file generated");
		} catch (ServiceException e) {
			logger.info("TimeZone tracker CSV file generation failed due to : [{}] {}", e.getErrorCode(),
					e.getMessage());
		} catch (Exception e) {
			logger.error("TimeZone tracker CSV file generation failed due to: " + e.getMessage(), e);
		}
	}

}
