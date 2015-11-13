package com.paxar.qps.common.config;

/**
 * This exception is thrown when some error was occurred during setup D2Comm configuration.
 * @author rsav
 *
 */
public class D2CommConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -4195326424183495618L;

	public D2CommConfigurationException(String message) {
		super(message);
	}

	public D2CommConfigurationException(String message, Throwable t) {
		super(message, t);
	}

}
