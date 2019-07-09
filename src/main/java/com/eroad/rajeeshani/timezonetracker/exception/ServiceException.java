/**
 * Handle all the service exceptions
 */
package com.eroad.rajeeshani.timezonetracker.exception;

/**
 * @author Rajeeshani
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    private final int errorCode;
    private final String message;

    public ServiceException(int errorCode, String message) {
    	super();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
