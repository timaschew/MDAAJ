package de.unikassel.mdda;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.unikassel.mdda.helper.MDDACodeGenerator;


public class MDDACodeGen<T> extends MDDABasic<T> {
	
	/**
	 * DO NOT CALL THIS CONSTRUCTOR<br>
	 * use {@link #createInstance(Class, int...)}
	 */
	public MDDACodeGen() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public static MDDACodeGen createInstance (Class<?> clazz, int... dimensions) {
		String className = null;
		try {
			if (clazz == null) {
				className = MDDACodeGenerator.createMultArray(Double.class, dimensions);
			} else {
				className = MDDACodeGenerator.createMultArray(clazz, dimensions);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// load class file
		ClassLoader classLoader = MDDACodeGen.class.getClassLoader();
		
		Class<?> loadedClass = null;
		try {
			loadedClass = classLoader.loadClass(className);
			MDDACodeGen<?> generatedArray = (MDDACodeGen<?>) loadedClass.newInstance();
			// not declaredFields, because multiArray it is inherited
			
			if (generatedArray.multiDimArray != null && generatedArray.multiDimArray.getClass().isArray()) {
				return generatedArray;
			} else {
				throw new IllegalArgumentException("array object is wrong = "+generatedArray.multiDimArray);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
