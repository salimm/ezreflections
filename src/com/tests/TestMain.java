package com.tests;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ezr.InMemoryEzReflectionsCompiler;

public class TestMain {
	public static void main(String[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException, NoSuchMethodException, SecurityException,
			ClassNotFoundException {
		InMemoryEzReflectionsCompiler compiler = new InMemoryEzReflectionsCompiler();
		String clsSrc = "" + "public class Test{ \n" + ""
				+ "public String test(){return \"Test\";}\n"
				+ "public static int testStatic(Integer i){return i;}\n" 
				+ "}";
		Class<?> cls = compiler.compileClass("Test", clsSrc);
		Object obj = cls.newInstance();
		Method method = obj.getClass().getDeclaredMethod("test",
				new Class<?>[] {});
		System.out.println("Test".equals(method.invoke(obj, new Object[] {})));
		
		Method method2 = obj.getClass().getMethod("testStatic",
				new Class<?>[] {Integer.class});
		System.out.println(new Integer(10).equals(method2.invoke(null, new Object[] {10})));
	}
}
