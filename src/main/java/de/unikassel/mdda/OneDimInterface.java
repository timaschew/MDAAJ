package de.unikassel.mdda;

public interface OneDimInterface<T> {
	
	/**
	 * @param index of the one dim array
	 * @return the element with the index for the one dimensional array
	 */
	public T get1D(int index);
	
	/**
	 * Sets the element with the index for the one dimensional array
	 * @param value to set
	 * @param index
	 * @return true if set operation was successful - otherwise false
	 */
	public boolean set1D(T value, int index);
	
	/**
	 * Converts the real 1-dimensonal index to the pseudo multidimensonal indices
	 * @param oneDimIndex
	 * @return
	 */
	public int[] getMultiDimIndices(int oneDimIndex);

}
