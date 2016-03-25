package com.ezr.loaders;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ezr.errors.PackageNotFoundException;

/**
 * This class provides utils to find classes and instantiate them
 * 
 * @author Salim
 *
 */
public class EzReflectionsClassLoader {
	private static final char PKG_SEPARATOR = '.';

	private static final char DIR_SEPARATOR = '/';

	private static final String CLASS_FILE_SUFFIX = ".class";

	/**
	 * This function returns list of Class objects for classes in a package
	 * 
	 * @param scannedPackage
	 * @return
	 * @throws PackageNotFoundException
	 */
	public List<Class<?>> find(String scannedPackage)
			throws PackageNotFoundException {
		String scannedPath = scannedPackage.replace(PKG_SEPARATOR,
				DIR_SEPARATOR);
		URL scannedUrl = Thread.currentThread().getContextClassLoader()
				.getResource(scannedPath);
		if (scannedUrl == null) {
			throw new PackageNotFoundException(scannedPackage);
		}
		File scannedDir = new File(scannedUrl.getFile());
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (File file : scannedDir.listFiles()) {
			classes.addAll(find(file, scannedPackage));
		}
		return classes;
	}

	private List<Class<?>> find(File file, String scannedPackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = scannedPackage + PKG_SEPARATOR + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(find(child, resource));
			}
		} else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
			int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException ignore) {
			}
		}
		return classes;
	}

	public Class<?> loadClass(String clsPath) throws ClassNotFoundException {
		return Class.forName(clsPath);
	}

}
