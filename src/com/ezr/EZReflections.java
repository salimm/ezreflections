package com.ezr;

public abstract class EZReflections {

	public EZReflections() {
		
	}
	
	/**
	 * This function creates 
	 * @param clsName
	 * @return 
	 */
	public abstract Class<?> compileClass(String clsName, String clsSrc);
}
