# ezReflections

Easy Relections is a minimal reflections library that supports useful tools for compiling java classes at runtime and searching java class structures.

It currently supports: 

1. Compiling classes and methods at runtime from string
2. Loading classes using name and classpath


Todo:

1. Search classes recursive declared variables
2. Search classes for recursive declared methods
3. Get list of methods accepting an argument
4. Create a maven project


#How to use
Simply download the jar file and include it in the classpath of your project.

#Tutorial
Using ezReflections is very easy. In this section you can find examples for its different features.


In order to compile a class source code into and excisting class you can use the function compileClass from EZReflectionsCompiler. The following code shows a simple example to do this.
```java
String clsSrc = "" + "public class Test{ \n" + ""
								+ "	public String test(){ \n"
								+ "		return \"Test\";\n"
								+ "	}\n"
								+ "}";
		Class<?> cls = compiler.compileClass("Test", clsSrc);
		Object obj = cls.newInstance();
		Method method = obj.getClass().getDeclaredMethod("test",
				new Class<?>[] {});
		System.out.println(method.invoke(obj, new Object[] {}));

```
After, compiling a class ezReflectionsCompiler returns an instance of java.lang.Class that can be used either by itself or through the tools in EzReflectionsExplorer to invoke methods and use variables.

Using EzReflectionsCompiler you can also compile static functions and receive an instance of Method that can be called by itself not needing to instantiate a method. The only important note is that you have to make sure you that you define your method static, otherwise, you will need an instance of the class to call the function (Simple java stuff). 

Also, private functions even if defined as static cannot be called because they are private. If you are familiar with private, protected and public in Java you know a private method can only be seen in the class itself. 

The compile Method functionality can be used to compile a piece of code and run it with needing to define a complicated class. However, the underlying library code creates an InMemory compilation of a clas with a unique random name that will not be used anymore.


```java
InMemoryEzReflectionsCompiler compiler = new InMemoryEzReflectionsCompiler();
		String clsSrc = "" + "public class Test{ \n" + ""
								+ "	public static int testStatic(Integer i){\n"
								+ "		return i;\n"
								+ "	}\n" 
								+ "}";
		Method method2 = obj.getClass().getMethod("testStatic",
				new Class<?>[] { Integer.class });
		System.out.println(new Integer(10).equals(method2.invoke(null,
				new Object[] { 10 })));
```

In order to compile and only get a static method to run you can use ezReflection as shown below:
```java
InMemoryEzReflectionsCompiler compiler = new InMemoryEzReflectionsCompiler();
		String methodSrc = "public static int testStatic(Integer i){\n"
				                +"		return i;\n" 
				                + "	}\n";

		Method method = compiler.compileStaticMethod("testStatic", methodSrc,
				new Class<?>[] { Integer.class });
		System.out.println(method.invoke(null, new Object[] { 10 }));
```
You can use EzReflectionsClassLoader to load a class or list of classes in a package from your code. The following examples show how to use EzReflectionsClassLoader

Example for loading a class using its class path

```java
	// testing class loader
		EzReflectionsClassLoader clsLoader = new EzReflectionsClassLoader();
		Class<?> strClass = clsLoader.loadClass(String.class.getName());
		String str = (String) strClass.getConstructor(
				new Class[] { String.class }).newInstance(
				new Object[] { "Test" });
		System.out.println(str);
```

Example for loading classes in a package:

```java
		// testing class loader to load classes in a package
		EzReflectionsClassLoader clsLoader = new EzReflectionsClassLoader();
		List<Class<?>> list = clsLoader.find("com.ezr.compiler");
		for (Class<?> cls : list) {
			System.out.println(cls.getName());
		}
```
