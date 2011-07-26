package de.unikassel.mdda;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MDDA<T> implements MDDAInterface<T>, OneDimInterface<T>, NeighborInterface<T>, Iterable<T> {
	
	/**
	 * Is the internal one dimensional array
	 */
	protected Object[] array = null;
	
	/**
	 * A dimension size array.<br>
	 * for example 
	 * <pre>int a[][] = new int[10][15]</pre> have the<br>
	 * dimension size array {10, 15}
	 */
	protected int[] size;
	
	/**
	 * The offset values for each dimension.<br>
	 * @see #calcOffset(int[])
	 */
	protected int[] sizeOffset;
	
	@SuppressWarnings("unchecked")
	public MDDA (int... dimensionSize) {
		
		int totalSize = 1;
		for (int i=0; i<dimensionSize.length; i++) {
			totalSize *= dimensionSize[i];
		}
		Object[] ar = new Object[totalSize];
		array =  (T[]) ar;

		this.size = dimensionSize;
		sizeOffset = calcOffset(dimensionSize);

	}

	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#get(int[])
	 */
	public T get(int... indices) {
		int index = calcOneDim(indices);
		return get1D(index);
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#set(java.lang.Object, int[])
	 */
	public boolean set(T value, int... indices) {
		int index = calcOneDim(indices);
		return set1D(value, index);
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.OneDimInterface#get1D(int)
	 */
	@SuppressWarnings("unchecked")
	public T get1D(int index) {
		return (T) array[index];
	}

	/* (non-Javadoc)
	 * @see de.unikassel.mdda.OneDimInterface#set1D(java.lang.Object, int)
	 */
	public boolean set1D(T value, int index) {
		Array.set(array, index, value);
		Object r = Array.get(array, index);
		if (value == null && r == null) {
			return true;
		} else if (value == null && r != null) {
			return false;
		} else if (value.equals(r)){
			return true;
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#fill(java.lang.Object)
	 */
	public boolean fill(T value) {
		for (int i=0 ; i<array.length; i++) {
			array[i] = value;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#flatten()
	 */
	@SuppressWarnings("unchecked")
	public T[] flatten() {
		return (T[]) array;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#getArray()
	 */
	public Object[] getArray() {
		return array;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#getSize()
	 */
	public int[] getSize() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#print()
	 */
	public void print() {
		print(System.out);
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#print(java.io.OutputStream)
	 */
	public void print(final OutputStream out) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<array.length; i++) {
			sb.append(checkNestedMod(i));
			 @SuppressWarnings("unchecked")
			T o = (T) array[i];
			 sb.append(o);
			 sb.append(" ");
			try {
				out.write(sb.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sb.append("\n");
	}

	public void prettyPrint() {
		
		int[] prevIndices = new int[size.length];
		boolean[] diff = new boolean[size.length];
		for (int i=0; i<prevIndices.length; i++) {
			prevIndices[i] = -1;
		}
		int[] tmpIndices;
		for (int index=0; index<array.length; index++) {
			tmpIndices = getMultiDimIndices(index);
			for (int j=0; j<prevIndices.length; j++) {
				diff[j] = prevIndices[j] == tmpIndices[j];
			}
			printLine(index, tmpIndices, diff);
			prevIndices = tmpIndices;
		}
		System.out.println();
	}
	
	private void printLine(int index, int[] indices, boolean[] diff) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<diff.length; i++) {
			if (diff[i] == false) {
				sb.append("[");
				sb.append(indices[i]);
				sb.append("]");
				sb.append("\t");
			} else {
				sb.append("\t");
			}
		}
		sb.append(get1D(index));
		System.out.println(sb.toString());
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.OneDimInterface#getMultiDimIndices(int)
	 */
	public int[] getMultiDimIndices(int oneDimIndex) {
		int[] tmpIndices = new int[size.length];
		int tmp = oneDimIndex;
		for (int i=0; i<sizeOffset.length-1; i++) {
			tmpIndices[i] = tmp / sizeOffset[i]; 
			tmp = tmp % sizeOffset[i];
		}
		tmpIndices[sizeOffset.length-1] = tmp;
		return tmpIndices;
	}

	/**
	 * example:<br>
	 * dimension [2][3][2][3]<br>
	 * has following offset array<br>
	 * [32][18][6][3] -> shift left -> [18][6][3][1]<br>
	 * multiplying backwards the dimension size<br>
	 * need for calculating index of pseudo multi dim<br>
	 * @param dimensionSize
	 * @return
	 */
	private int[] calcOffset(int[] dimensionSize) {
		int[] offsets = new int[dimensionSize.length];
		int offset = 1;
		for (int i = dimensionSize.length-1; i>=0; i--) {
			offsets[i] = offset;
			offset *= dimensionSize[i];
		}
		return offsets;
	}
	
	/**
	 * @param indices
	 * @return
	 */
	public int calcOneDim(int...indices) {
		if (indices.length != size.length) {
			throw new IllegalArgumentException("indices length for wrong dimension: "+indices.length);
		}
		for (int i=0; i<size.length; i++) {
			if (indices[i] < 0 || indices[i] > size[i]-1) {
				throw new ArrayIndexOutOfBoundsException("index is out of dimension bound: "+indices[i]);
			}
		}
		int index = 0;
		for (int i=0; i<indices.length-1; i++) {
			index += sizeOffset[i] * indices[i];
		}
		index += indices[indices.length-1];
		return index;
	}
    
    private String checkNestedMod(int index) {
    	StringBuilder sb = new StringBuilder();
    	if (index == 0) {
    		return "";
    	}
    	for (int i=sizeOffset.length-2; i>=0; i--) {
    			boolean tmp = index % sizeOffset[i] == 0;
    			if (tmp == false) {
    				break;
    			}
    			sb.append("\n"); // else add new line
    	}
    	return sb.toString(); // return true;
	}
    
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.NeighborInterface#getNeighborForAllDims(int, int[])
	 */
	public Set<Integer> getNeighborForAllDims(int distance, int... indices) {
		Set<Integer> set = new HashSet<Integer>();
		int index = calcOneDim(indices);
		set.add(index);
		Set<Integer> resultSet = getNeighborForAllDims(distance, set, indices);
		resultSet.remove(index);
		return resultSet;
	}
	
	private Set<Integer> getNeighborForAllDims(int distance, Set<Integer> alreadyIncluded, int... indices) {
		calcOneDim(indices); // check range
		
		Set<Integer> list = new HashSet<Integer>();
		for (int i=size.length-1; i>=0; i--) {
			list.addAll(getNeighborForAllDims(indices, i, alreadyIncluded));
		}
		if (distance > 1) {
			// copy set, because list will be modified in the loop (avoid ConcurentModificationException) 
			for (Integer neighborIndex : new HashSet<Integer>(list)) {
				int[] neighborIndices = getMultiDimIndices(neighborIndex);
				list.addAll(getNeighborForAllDims(distance-1, list, neighborIndices));
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.NeighborInterface#getNeighborForDim(int, int, int[])
	 */
	public Set<Integer> getNeighborForDim(int distance, int dimension, int... indices) {
		Set<Integer> set = new HashSet<Integer>();
		int index = calcOneDim(indices);
		set.add(index);
		Set<Integer> resultSet = getNeighborForDim(distance, dimension, set, indices);
		resultSet.remove(index);
		return resultSet;
	}
	
	private Set<Integer> getNeighborForDim(int distance, int dimension, Set<Integer> ignoreValues, int... indices) {
		if (dimension < 0 || dimension > size.length-1) {
			throw new ArrayIndexOutOfBoundsException("dimension does not exist: "+dimension);
		}
		calcOneDim(indices); // check range
		Set<Integer> list = new HashSet<Integer>();
		list.addAll(getNeighborForAllDims(indices, dimension, ignoreValues));
		
		if (distance > 1) {
			// copy set, because list will be modified in the loop (avoid ConcurentModificationException) 
			for (Integer neighborIndex : new HashSet<Integer>(list)) {
				int[] neighborIndices = getMultiDimIndices(neighborIndex);
				list.addAll(getNeighborForDim(distance-1, dimension, list, neighborIndices));
			}
		}
		return list;
	}
	
	private Set<Integer> getNeighborForAllDims(int[] indices, int dim, Set<Integer> ignoreValues) {
		boolean ignore = ignoreValues != null && ignoreValues.size() > 0;
		Set<Integer> list = new HashSet<Integer>();
		int[] tmpIndices = Arrays.copyOf(indices, indices.length);
		
		try {
			tmpIndices[dim] = indices[dim]-1;
    		int negativeIndex = calcOneDim(tmpIndices);
    		if (negativeIndex >= 0 && negativeIndex < array.length) {
    			if (ignore && ignoreValues.contains(negativeIndex) == false || ignore == false) {
    				list.add(negativeIndex); // negative
    			}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		} try {
    		tmpIndices[dim] = indices[dim]+1;
    		int positiveIndex = calcOneDim(tmpIndices);
    		if (positiveIndex >= 0 && positiveIndex < array.length) {
    			if (ignore && ignoreValues.contains(positiveIndex) == false || ignore == false) {
    				list.add(positiveIndex); // negative
    			}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		}
    	
    	return list;
	}


	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = 0;
			public boolean hasNext() {
				return index < array.length;
			}

			public T next() {
				@SuppressWarnings("unchecked")
				T r = (T) array[index];
				index++;
				return r;
			}

			public void remove() {
				System.err.println("not implemted");
			}
		};
	}

}
