package com.ezr.compiler;

import java.net.URL;

/**
 * A class loader for a single class. Thanks to
 * http://normalengineering.com.au/normality
 * /c-and-java-code-snippets/java-runtime-compilation-in-memory/ for this
 * section
 */
public class SingleClassLoader extends ClassLoader {
	/**
	 * holds the ByteCode in memory
	 */
	private final ByteCode byteCode;
	private String clsName;
	
	public SingleClassLoader(URL[] urls, ByteCode byteCode) {
		super(Thread.currentThread().getContextClassLoader());
		this.clsName = byteCode.getClassName();
		this.byteCode = byteCode;
	}

	public Class<?> lookup(String className)
			throws ClassNotFoundException {
		if(clsName.equals(className)){
			return defineClass(className, byteCode.getByteCode(), 0,
					byteCode.getByteCode().length);
		}else{
			return super.findClass(className);
		}
	}

	ByteCode getFileObject() {
		return byteCode;
	}

}