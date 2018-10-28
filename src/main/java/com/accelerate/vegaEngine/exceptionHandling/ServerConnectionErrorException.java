package com.accelerate.vegaEngine.exceptionHandling;

public class ServerConnectionErrorException extends Exception {

	private static final long serialVersionUID = -470180507998010368L;

	public ServerConnectionErrorException() {
		super();
	}

	public ServerConnectionErrorException(final String message) {
		super(message);
	}
}