package com.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ezr.compiler.InMemoryEzReflectionsCompiler;

public class TestMain {
	public static void main(String[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException, NoSuchMethodException, SecurityException,
			ClassNotFoundException {
		InMemoryEzReflectionsCompiler compiler = new InMemoryEzReflectionsCompiler();
		String clsSrc = "" + "public class Test{ \n" + ""
				+ "	public String test(){ \n" + "		return \"Test\";\n" + "	}\n"
				+ "	public static int testStatic(Integer i){\n"
				+ "		return i;\n" + "	}\n" + "}";

		Class<?> cls = compiler.compileClass("Test", clsSrc);
		Object obj = cls.newInstance();
		Method method = obj.getClass().getDeclaredMethod("test",
				new Class<?>[] {});

		System.out.println("Test".equals(method.invoke(obj, new Object[] {})));

		Method method2 = obj.getClass().getMethod("testStatic",
				new Class<?>[] { Integer.class });
		System.out.println(new Integer(10).equals(method2.invoke(null,
				new Object[] { 10 })));
		String methodSrc = "	public static int testStatic(Integer i){\n"
				+ "		return i;\n" + "	}\n";

		Method method3 = compiler.compileStaticMethod("testStatic", methodSrc,
				new Class<?>[] { Integer.class });
		System.out.println(method3.invoke(null, new Object[] { 10 }));
	}

}
