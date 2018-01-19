package com.ezr.compiler;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class InMemoryFileObject extends SimpleJavaFileObject {
	private String code;

	protected InMemoryFileObject(String name, String code) {
		super(URI.create("file:////" + name.replace('.', '/')
				+ Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
