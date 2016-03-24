package com.ezr.compiler;

import java.util.ArrayList;
import java.util.Iterator;

class InMemoryJavaFileObjectList implements Iterable<InMemoryFileObject> {
	ArrayList<InMemoryFileObject> list;

	public InMemoryJavaFileObjectList() {
		list = new ArrayList<InMemoryFileObject>();
	}

	public InMemoryJavaFileObjectList(ArrayList<String> clsNames,
			ArrayList<String> list) {
		this.list = new ArrayList<InMemoryFileObject>();
		int index = 0;
		for (String str : list) {
			this.list.add(new InMemoryFileObject(clsNames.get(index), str));
			index++;
		}
	}

	public void addSrcString(String clsName, String src) {
		this.list.add(new InMemoryFileObject(clsName, src));
	}

	@Override
	public Iterator<InMemoryFileObject> iterator() {

		return list.iterator();
	}

}
