package com.fantosoft.arest.exception;

import com.fantosoft.arest.entity.DemoUser;

public class CustomErrorType extends DemoUser {

	private String errorMessage;

	public CustomErrorType(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	public String getErrorMessage() {
		return errorMessage;
	}

}
