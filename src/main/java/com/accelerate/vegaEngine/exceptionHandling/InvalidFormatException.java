package com.accelerate.vegaEngine.exceptionHandling;

public class InvalidFormatException extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public InvalidFormatException() {
		super();
	}

	public InvalidFormatException(final String message) {
		super(message);
	}

}