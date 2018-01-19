package com.ezr.compiler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.ezr.errors.NotAStaticMethodException;

public class InMemoryEzReflectionsCompiler extends EZReflectionsCompiler {

	@Override
	public Class<?> compileClass(String clsName, String clsSrc, List<String> options)
			throws ClassNotFoundException, IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		InMemoryJavaFileObjectList fileObjects = new InMemoryJavaFileObjectList();
		@SuppressWarnings("rawtypes")
		DiagnosticCollector diagnosticListener = new DiagnosticCollector();

		List<File> paths = createPath(options);
		StandardJavaFileManager sfm = compiler.getStandardFileManager(null, null, null);
		if (paths != null) {
			sfm.setLocation(StandardLocation.CLASS_PATH, paths);
		}

		SingleFileManager singleFileManager = new SingleFileManager(sfm, createPathURLs(options),
				new ByteCode(clsName));
		fileObjects.addSrcString(clsName, clsSrc);
		@SuppressWarnings("unchecked")
		CompilationTask compile = compiler.getTask(null, singleFileManager, diagnosticListener, options, null,
				fileObjects);
		if (!compile.call()) {
			for (Object diagnostic : diagnosticListener.getDiagnostics()) {
				System.err.println(diagnostic);
			}
		} else {
			try {
				return singleFileManager.getClassLoader().lookup(clsName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<File> createPath(List<String> options) {
		if (options == null) {
			return null;
		}
		List<File> paths = null;
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).equals("-classpath") || options.get(i).equals("-cp")) {
				paths = new ArrayList<File>();
				String[] parts = options.get(i + 1).split(":");
				for (String path : parts) {
					paths.add(new File(path));
				}
			}
		}
		return paths;
	}

	private URL[] createPathURLs(List<String> options) throws MalformedURLException {
		if (options == null) {
			return new URL[] {};
		}
		URL[] paths = null;
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).equals("-classpath") || options.get(i).equals("-cp")) {
				String[] parts = options.get(i + 1).split(":");
				paths = new URL[parts.length];
				for (int j = 0; j < parts.length; j++) {
					paths[j] = new URL("file:" + parts[j]);
				}
			}
		}
		return paths;
	}

	@Override
	public Method compileStaticMethod(String methodName, String methodSrc, Class<?>[] parameterTypes)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, NotAStaticMethodException,
			IOException {
		Pattern p = Pattern.compile("^[ |\t|\r|\n|\r|\b]*public[ |\t|\r|\n|\r|\b]+static.*");

		if (!p.matcher(methodSrc).find()) {
			throw new NotAStaticMethodException(methodName, methodSrc);
		}
		String clsName = "TempClass" + System.currentTimeMillis();
		String clsSrc = "public class " + clsName + " { \n" + "" + methodSrc + " \n" + "}";
		Class<?> cls = compileClass(clsName, clsSrc);
		return cls.getDeclaredMethod(methodName, parameterTypes);
	}

}
