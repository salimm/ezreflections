package com.ezr.compiler;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

/**
 * A file manager for a single class. Thanks to
 * http://normalengineering.com.au/normality
 * /c-and-java-code-snippets/java-runtime-compilation-in-memory/ for this
 * section
 * */
@SuppressWarnings("rawtypes")
public class SingleFileManager extends ForwardingJavaFileManager {

	private final SingleClassLoader singleClassLoader;

	@SuppressWarnings("unchecked")
	public SingleFileManager(JavaCompiler compiler, ByteCode byteCode) {
		super(compiler.getStandardFileManager(null, null, null));
		singleClassLoader = new SingleClassLoader(byteCode);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location notUsed,
			String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		return singleClassLoader.getFileObject();
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return singleClassLoader;
	}

	public SingleClassLoader getClassLoader() {
		return singleClassLoader;
	}

}