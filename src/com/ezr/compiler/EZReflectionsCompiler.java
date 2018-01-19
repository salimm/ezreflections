package com.ezr.compiler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
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
	 * @throws IOException 
	 */
	public abstract Class<?> compileClass(String clsName, String clsSrc, List<String> options)
			throws ClassNotFoundException, IOException;
	
	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 * @throws IOException 
	 */
	public  Class<?> compileClass(String clsName, String clsSrc)
			throws ClassNotFoundException, IOException{
		return compileClass(clsName, clsSrc, null);
	}

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 * @throws IOException 
	 */
	public Class<?> compileClass(String clsSrc, List<String> options) throws ClassNotFoundException, IOException {
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
	 * @throws IOException 
	 */
	public Class<?> compileClass(String clsSrc) throws ClassNotFoundException, IOException {
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
	 * @throws IOException 
	 */
	public abstract Method compileStaticMethod(String methodName,
			String methodSrc, Class<?>[] parameterTypes)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, NotAStaticMethodException, IOException;

	/**
	 * This function compiles a class and returns a class object from java.lang
	 * 
	 * @param clsName
	 * @return
	 * @throws NotAStaticMethodException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IOException 
	 */
	public Method compileStaticMethod(String methodSrc,
			Class<?>[] parameterTypes) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, NotAStaticMethodException, IOException {
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
