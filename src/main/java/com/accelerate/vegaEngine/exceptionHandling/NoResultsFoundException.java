package com.accelerate.vegaEngine.exceptionHandling;

public class NoResultsFoundException extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public NoResultsFoundException() {
		super();
	}

	public NoResultsFoundException(final String message) {
		super(message);
	}

}