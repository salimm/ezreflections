package com.ezr.compiler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Container for Java byte code in memory. Thanks to
 * http://normalengineering.com
 * .au/normality/c-and-java-code-snippets/java-runtime-compilation-in-memory/
 * for this section
 */
public class ByteCode extends SimpleJavaFileObject {

	private ByteArrayOutputStream byteArrayOutputStream;
	private String className;

	public ByteCode(String className) {
		super(URI.create("byte:///" + className + ".class"), Kind.CLASS);
		this.setClassName(className);
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return null;
	}

	@Override
	public OutputStream openOutputStream() {
		byteArrayOutputStream = new ByteArrayOutputStream();
		return byteArrayOutputStream;
	}

	@Override
	public InputStream openInputStream() {
		return null;
	}

	public byte[] getByteCode() {
		return byteArrayOutputStream.toByteArray();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}