package com.ezr;

public abstract class EZReflectionsCompiler {

	public EZReflectionsCompiler() {

	}

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 */
	public abstract Class<?> compileClass(String clsName, String clsSrc)
			throws ClassNotFoundException;

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 */
	public abstract Class<?> compileMethod(String methodName, String methodSrc);

}
