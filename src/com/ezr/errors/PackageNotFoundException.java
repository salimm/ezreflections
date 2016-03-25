package com.ezr.errors;

public class PackageNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PackageNotFoundException(String clsPath) {
		super("Unable to get resources from path " + clsPath + ".");
	}

}
