package com.ezr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class InMemoryEzReflectionsCompiler extends EZReflectionsCompiler {

	@Override
	public Class<?> compileClass(String clsName, String clsSrc)
			throws ClassNotFoundException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		InMemoryJavaFileObjectList fileObjects = new InMemoryJavaFileObjectList();
		@SuppressWarnings("rawtypes")
		DiagnosticCollector diagnosticListener = new DiagnosticCollector<InMemoryFileObject>();
		SingleFileManager singleFileManager = new SingleFileManager(compiler,
				new ByteCode(clsName));
		fileObjects.addSrcString(clsSrc);
		@SuppressWarnings("unchecked")
		CompilationTask compile = compiler.getTask(null, singleFileManager,
				diagnosticListener, null, null, fileObjects);
		if (!compile.call()) {
			/* Compilation failed: Output the compiler errors to stderr. */
			for (Object diagnostic : diagnosticListener.getDiagnostics()) {
				System.err.println(diagnostic);
			}
		} else {
			/* Compilation succeeded: Get the Class object. */
			try {
				Class<?> cls = singleFileManager.getClassLoader().findClass(
						clsName);
				return cls;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Method compileStaticMethod(String methodName, String methodSrc,
			Class<?>[] parameterTypes) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException {
		String clsName = "TempClass" + System.currentTimeMillis();
		String clsSrc = "public class " + clsName + " { \n" + "" + methodSrc
				+ " \n" + "}";

		Class<?> cls = compileClass(clsName, clsSrc);
		return cls.getDeclaredMethod(clsName, parameterTypes);
	}

	/* A file manager for a single class. */
	private static class SingleFileManager extends ForwardingJavaFileManager {

		@SuppressWarnings("unchecked")
		public SingleFileManager(JavaCompiler compiler, ByteCode byteCode) {
			super(compiler.getStandardFileManager(null, null, null));
			singleClassLoader_ = new SingleClassLoader(byteCode);
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location notUsed,
				String className, JavaFileObject.Kind kind, FileObject sibling)
				throws IOException {
			return singleClassLoader_.getFileObject();
		}

		@Override
		public ClassLoader getClassLoader(Location location) {
			return singleClassLoader_;
		}

		public SingleClassLoader getClassLoader() {
			return singleClassLoader_;
		}

		private final SingleClassLoader singleClassLoader_;
	}

}

class InMemoryJavaFileObjectList implements Iterable<InMemoryFileObject> {
	ArrayList<InMemoryFileObject> list;

	public InMemoryJavaFileObjectList() {
		list = new ArrayList<InMemoryFileObject>();
	}

	public InMemoryJavaFileObjectList(ArrayList<String> list) {
		this.list = new ArrayList<InMemoryFileObject>();
		for (String str : list) {
			this.list.add(new InMemoryFileObject("Test", str));
		}
	}

	public void addSrcString(String src) {
		this.list.add(new InMemoryFileObject("Test", src));
	}

	@Override
	public Iterator<InMemoryFileObject> iterator() {

		return list.iterator();
	}

}

class InMemoryFileObject extends SimpleJavaFileObject {
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

/* A file manager for a single class. */
class SingleFileManager extends ForwardingJavaFileManager {
	private final SingleClassLoader singleClassLoader_;

	@SuppressWarnings("unchecked")
	public SingleFileManager(JavaCompiler compiler, ByteCode byteCode) {
		super(compiler.getStandardFileManager(null, null, null));
		singleClassLoader_ = new SingleClassLoader(byteCode);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location notUsed,
			String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		return singleClassLoader_.getFileObject();
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return singleClassLoader_;
	}

	public SingleClassLoader getClassLoader() {
		return singleClassLoader_;
	}

}

/* A class loader for a single class. */
class SingleClassLoader extends ClassLoader {

	public SingleClassLoader(ByteCode byteCode) {
		byteCode_ = byteCode;
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		return defineClass(className, byteCode_.getByteCode(), 0,
				byteCode_.getByteCode().length);
	}

	ByteCode getFileObject() {
		return byteCode_;
	}

	private final ByteCode byteCode_;
}

/* Container for Java byte code in memory. */
class ByteCode extends SimpleJavaFileObject {

	public ByteCode(String className) {
		super(URI.create("byte:///" + className + ".class"), Kind.CLASS);
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return null;
	}

	@Override
	public OutputStream openOutputStream() {
		byteArrayOutputStream_ = new ByteArrayOutputStream();
		return byteArrayOutputStream_;
	}

	@Override
	public InputStream openInputStream() {
		return null;
	}

	public byte[] getByteCode() {
		return byteArrayOutputStream_.toByteArray();
	}

	private ByteArrayOutputStream byteArrayOutputStream_;
}