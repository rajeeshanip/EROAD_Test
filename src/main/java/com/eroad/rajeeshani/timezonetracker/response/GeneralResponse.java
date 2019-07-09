/**
 * General response class
 */
package com.eroad.rajeeshani.timezonetracker.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rajeeshani
 */
@ApiModel("General Response")
@ToString
@Getter
@Setter
public class GeneralResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("Status code")
	private int statusCode;
	@ApiModelProperty("Message")
	private String message;

}
