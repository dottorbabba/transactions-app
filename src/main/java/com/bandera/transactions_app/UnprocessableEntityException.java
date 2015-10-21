package com.bandera.transactions_app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2206859247547457584L;

	public UnprocessableEntityException(String message) {
		super(message);
	}
	
}
