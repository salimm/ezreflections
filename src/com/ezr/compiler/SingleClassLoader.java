package com.ezr.compiler;

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

	public SingleClassLoader(ByteCode byteCode) {
		this.byteCode = byteCode;
	}

	@Override
	public Class<?> findClass(String className)
			throws ClassNotFoundException {
		return defineClass(className, byteCode.getByteCode(), 0,
				byteCode.getByteCode().length);
	}

	ByteCode getFileObject() {
		return byteCode;
	}

}