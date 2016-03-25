package com.tests;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ezr.errors.PackageNotFoundException;
import com.ezr.loaders.EzReflectionsClassLoader;

public class TestClassLoader {

	public static void main(String[] args) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, PackageNotFoundException {
		// testing class loader
		EzReflectionsClassLoader clsLoader = new EzReflectionsClassLoader();
		Class<?> strClass = clsLoader.loadClass(String.class.getName());
		String str = (String) strClass.getConstructor(
				new Class[] { String.class }).newInstance(
				new Object[] { "Test" });
		System.out.println(str);

		// testing class loader to load classes in a package

		List<Class<?>> list = clsLoader.find("com.ezr.compiler");
		for (Class<?> cls : list) {
			System.out.println(cls.getName());
		}
		
		// 

	}
}
