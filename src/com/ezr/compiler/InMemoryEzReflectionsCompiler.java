package com.ezr.compiler;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.ToolProvider;

import com.ezr.errors.NotAStaticMethodException;

public class InMemoryEzReflectionsCompiler extends EZReflectionsCompiler {

	@Override
	public Class<?> compileClass(String clsName, String clsSrc)
			throws ClassNotFoundException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		InMemoryJavaFileObjectList fileObjects = new InMemoryJavaFileObjectList();
		@SuppressWarnings("rawtypes")
		DiagnosticCollector diagnosticListener = new DiagnosticCollector();

		SingleFileManager singleFileManager = new SingleFileManager(compiler,
				new ByteCode(clsName));
		fileObjects.addSrcString(clsName, clsSrc);
		@SuppressWarnings("unchecked")
		CompilationTask compile = compiler.getTask(null, singleFileManager,
				diagnosticListener, null, null, fileObjects);
		if (!compile.call()) {
			for (Object diagnostic : diagnosticListener.getDiagnostics()) {
				System.err.println(diagnostic);
			}
		} else {
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
			NoSuchMethodException, SecurityException, NotAStaticMethodException {
		Pattern p = Pattern
				.compile("^[ |\t|\r|\n|\r|\b]*public[ |\t|\r|\n|\r|\b]+static.*");

		if (!p.matcher(methodSrc).find()) {
			throw new NotAStaticMethodException(methodName, methodSrc);
		}
		String clsName = "TempClass" + System.currentTimeMillis();
		String clsSrc = "public class " + clsName + " { \n" + "" + methodSrc
				+ " \n" + "}";
		Class<?> cls = compileClass(clsName, clsSrc);
		return cls.getDeclaredMethod(methodName, parameterTypes);
	}
}
