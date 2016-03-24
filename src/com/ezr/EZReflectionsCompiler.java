package com.ezr;

import java.lang.reflect.Method;

public abstract class EZReflectionsCompiler  {

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
	 * This function returns a Method object from a static function. For none
	 * static use Class because the obj is needed to call the method
	 * 
	 * @param clsName
	 * @return
	 */
	public abstract Method compileStaticMethod(String methodName, String methodSrc,
			Class<?>[] parameterTypes) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException ;

}
