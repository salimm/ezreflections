package com.ezr.compiler;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import com.ezr.errors.NotAStaticMethodException;

public abstract class EZReflectionsCompiler {

	public EZReflectionsCompiler() {

	}

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @param clsSrc
	 * @param options: list of compiler task options
	 * @return
	 */
	public abstract Class<?> compileClass(String clsName, String clsSrc, Iterable<String> options)
			throws ClassNotFoundException;
	
	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 */
	public  Class<?> compileClass(String clsName, String clsSrc)
			throws ClassNotFoundException{
		return compileClass(clsName, clsSrc, null);
	}

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 */
	public Class<?> compileClass(String clsSrc, Iterable<String> options) throws ClassNotFoundException {
		String clsName = null;

		Pattern p = Pattern
				.compile(".*class[ |\t|\r|\n|\r|\b]+([a-z|A-z|0-9|_|-]+)[ |\t|\r|\n|\r|\b]*[\\{|extends|implements].*");
		Matcher matcher = p.matcher(clsSrc);

		if (matcher.find()) {
			clsName = matcher.group(1);
		} else {
			throw new SyntaxException(
					"Class does not match syntax. Cannot extract the class name");
		}
		return compileClass(clsName, clsSrc,options);
	}
	
	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 */
	public Class<?> compileClass(String clsSrc) throws ClassNotFoundException {
		String clsName = null;

		Pattern p = Pattern
				.compile(".*class[ |\t|\r|\n|\r|\b]+([a-z|A-z|0-9|_|-]+)[ |\t|\r|\n|\r|\b]*[\\{|extends|implements].*");
		Matcher matcher = p.matcher(clsSrc);

		if (matcher.find()) {
			clsName = matcher.group(1);
		} else {
			throw new SyntaxException(
					"Class does not match syntax. Cannot extract the class name");
		}
		return compileClass(clsName, clsSrc);
	}

	/**
	 * This function returns a Method object from a static function. For none
	 * static use Class because the obj is needed to call the method
	 * 
	 * @param clsName
	 * @return
	 */
	public abstract Method compileStaticMethod(String methodName,
			String methodSrc, Class<?>[] parameterTypes)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, NotAStaticMethodException;

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 * @throws NotAStaticMethodException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public Method compileStaticMethod(String methodSrc,
			Class<?>[] parameterTypes) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, NotAStaticMethodException {
		String methodName = null;

		Pattern p = Pattern
				.compile("^[ |\t|\r|\n|\r|\b]*public[ |\t|\r|\n|\r|\b]+.*[ |\t|\r|\n|\r|\b]([a-z|A-z|0-9|_|-]+)\\(");
		Matcher matcher = p.matcher(methodSrc);

		if (matcher.find()) {
			methodName = matcher.group(1);
		} else {
			throw new SyntaxException(
					"Class does not match syntax. Cannot extract the class name");
		}
		return compileStaticMethod(methodName, methodSrc, parameterTypes);
	}
}
