package de.unikassel.mdda;

public interface PseudoInterface<T> {
	
	public T getPseudo(int index);
	
	public boolean setPseudo(T value, int index);
	
	/**
	 * Converts the real 1-dimensonal index to the pseudo multidimensonal indices
	 * @param oneDimIndex
	 * @return
	 */
	public int[] getMultiDimIndices(int oneDimIndex);

}
