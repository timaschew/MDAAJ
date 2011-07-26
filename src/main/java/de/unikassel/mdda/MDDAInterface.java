package de.unikassel.mdda;

import java.io.OutputStream;

public interface MDDAInterface<T> {
	
	/**
	 * Returns the multi dim element
	 * You should pass for each dimension an index - otherwise you get an ecxception
	 * @param indices for each dimension one index as var args
	 * @return the element of the multi dimensional array
	 * @exception IllegalArgumentException
	 */
	public T get(int... indices);
	
	/**
	 * Sets the multi dim element
	 * You should pass for each dimension an index - otherwise you get an ecxception
	 * @param value to set
	 * @param indices for each dimension one index as var args
	 * @return true if operation was successful - otherwise false
	 * @exception IllegalArgumentException
	 */
	public boolean set(T value, int... indices);
	
	/**
	 * Sets all elements with the given value
	 * @param value to set
	 * @return true if the operation was successful - otherwise false
	 */
	public boolean fill(T value);
	
	/**
	 * Returns the array as one dimension array.<br>
	 * example {{1,2,3},{4,5,6}} becomes {1,2,3,4,5,6}
	 * @return the multi dim array as one dim array
	 */
	public T[] flatten();
	
	/**
	 * Prints the array in simple format using System.out as default outputstream
	 */
	public void print();
	
	/**
	 * Prints the array into the specifiy outputstream
	 * @param out
	 */
	public void print(OutputStream out);
	
	/**
	 * @return the raw array object
	 */
	public Object getArray();
	
	/**
	 * @return an array of each dimension size as element
	 */
	public int[] getSize();

}
