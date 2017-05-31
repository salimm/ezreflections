package com.ezr.loaders;

import java.util.ArrayList;
import java.util.List;

public class EZPackageLoader {
	/**
	 * Returns pacage with exact name if exists
	 * 
	 * @param name
	 * @return
	 */
	public static Package findPackage(String name) {
		return Package.getPackage(name);
	}

	/**
	 * find packages with prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public static List<Package> findPackageWithPrefix(String prefix) {
		Package[] packages = Package.getPackages();
		List<Package> out = new ArrayList<Package>();
		for (Package pkg : packages) {
			if (pkg.getName().startsWith(prefix)) {
				out.add(pkg);
			}
		}
		return out;
	}
}
