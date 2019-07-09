/**
 * 
 */
package com.eroad.rajeeshani.timezonetracker.util;

/**
 * @author Rajeeshani_inova
 *
 */
public class ErrorCodes {
	private ErrorCodes() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static final int SUCCESS = 0;
	public static final int UNKNOWN_ERROR = -99;
	
	public static final int INVALID_INPUT=101;
}
