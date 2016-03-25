package com.ezr.errors;

public class NotAStaticMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAStaticMethodException(String methodName, String methodSrc) {
		super(" Method " + methodName + " is not a static method. Source: "
				+ methodSrc);
	}
}
