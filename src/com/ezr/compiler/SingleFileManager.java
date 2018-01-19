package com.ezr.compiler;

import java.io.IOException;
import java.net.URL;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;


/**
 * A file manager for a single class. Thanks to
 * http://normalengineering.com.au/normality
 * /c-and-java-code-snippets/java-runtime-compilation-in-memory/ for this
 * section
 */
@SuppressWarnings("rawtypes")
public class SingleFileManager extends ForwardingJavaFileManager {

	private final SingleClassLoader singleClassLoader;

	@SuppressWarnings("unchecked")
	public SingleFileManager(JavaFileManager manager, URL[] paths, ByteCode byteCode) {
		super(manager);
		singleClassLoader = new SingleClassLoader(paths,byteCode);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location notUsed, String className, JavaFileObject.Kind kind,
			FileObject sibling) throws IOException {
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
