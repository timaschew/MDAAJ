package de.unikassel.mdda;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MDDAPseudo<T> implements MDDAInterface<T>, PseudoInterface<T>, NeighborInterface<T> {
	
	/**
	 * Have to be initialzed by the subclass
	 */
	protected T[] multiDimArray = null;
	protected int[] dimensionSizeArray;
	protected int[] dimensionOffset;
	
	public MDDAPseudo (int... dimensionSize) {
		
		int totalSize = 1;
		for (int i=0; i<dimensionSize.length; i++) {
			totalSize *= dimensionSize[i];
		}
		Object[] array = new Object[totalSize];
		multiDimArray =  (T[]) array;

		this.dimensionSizeArray = dimensionSize;
		dimensionOffset = calcOffset(dimensionSize);

	}

	
	public T get(int... indices) {
		int index = calcOneDim(indices);
		return getPseudo(index);
	}
	
	public boolean set(T value, int... indices) {
		int index = calcOneDim(indices);
		return setPseudo(value, index);
	}
	

	public T getPseudo(int index) {
		return multiDimArray[index];
	}


	public boolean setPseudo(T value, int index) {
		Array.set(multiDimArray, index, value);
		Object r = Array.get(multiDimArray, index);
		if (value.equals(r)){
			return true;
		}
		return false;
	}


	public boolean fill(T value) {
		for (int i=0 ; i<multiDimArray.length; i++) {
			multiDimArray[i] = value;
		}
		return true;
	}

	public T[] flatten() {
		return multiDimArray;
	}
	
	public Object getArray() {
		return multiDimArray;
	}
	
	public void print() {
		for (int i=0; i<multiDimArray.length; i++) {
			String skip = checkNestedMod(i);
			 T o = multiDimArray[i];
			System.out.print(skip+o+" ");
		}
		System.out.println();
	}

	public void prettyPrint() {
		
		int[] prevIndices = new int[dimensionSizeArray.length];
		boolean[] diff = new boolean[dimensionSizeArray.length];
		for (int i=0; i<prevIndices.length; i++) {
			prevIndices[i] = -1;
		}
		int[] tmpIndices;
		for (int index=0; index<multiDimArray.length; index++) {
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
		sb.append(getPseudo(index));
		System.out.println(sb.toString());
	}
	
	public int[] getMultiDimIndices(int oneDimIndex) {
		int[] tmpIndices = new int[dimensionSizeArray.length];
		int tmp = oneDimIndex;
		for (int i=0; i<dimensionOffset.length-1; i++) {
			tmpIndices[i] = tmp / dimensionOffset[i]; 
			tmp = tmp % dimensionOffset[i];
		}
		tmpIndices[dimensionOffset.length-1] = tmp;
		return tmpIndices;
	}

	/**
	 * dimension [2][3][2][3]
	 * has following offset array
	 * [32][18][6][3] -> shift left -> [18][6][3][1]
	 * multiplying backwards the dimension size
	 * need for calculating index of pseudo multi dim
	 * @param dimensionSize
	 * @return
	 */
	private static int[] calcOffset(int[] dimensionSize) {
		int[] offsets = new int[dimensionSize.length];
		int offset = 1;
		for (int i = dimensionSize.length-1; i>=0; i--) {
			offsets[i] = offset;
			offset *= dimensionSize[i];
		}
		return offsets;
	}
	
	public int calcOneDim(int...indices) {
		if (indices.length != dimensionSizeArray.length) {
			throw new IllegalArgumentException("indices length for wrong dimension: "+indices.length);
		}
		for (int i=0; i<dimensionSizeArray.length; i++) {
			if (indices[i] < 0 || indices[i] > dimensionSizeArray[i]-1) {
				throw new ArrayIndexOutOfBoundsException("index is out of dimension bound: "+indices[i]);
			}
		}
		int index = 0;
		for (int i=0; i<indices.length-1; i++) {
			index += dimensionOffset[i] * indices[i];
		}
		index += indices[indices.length-1];
		return index;
	}
    
    private String checkNestedMod(int index) {
    	if (index == 0) {
    		return "";
    	}
    	String skip = "";
    	for (int i=dimensionOffset.length-2; i>=0; i--) {
    			boolean tmp = index % dimensionOffset[i] == 0;
    			if (tmp == false) {
    				break;
    			}
    			skip += "\n"; // else add new line
    	}
    	return skip; // return true;
	}
    
	public Set<Integer> getNeighborForAllDims(int distance, int... indices) {
		Set<Integer> set = new HashSet<Integer>();
		int index = calcOneDim(indices);
		set.add(index);
		Set<Integer> resultSet = getNeighborForAllDims(distance, set, indices);
		resultSet.remove(index);
		return resultSet;
	}
	
	public Set<Integer> getNeighborForAllDims(int distance, Set<Integer> alreadyIncluded, int... indices) {
		calcOneDim(indices); // check range
		
		Set<Integer> list = new HashSet<Integer>();
		for (int i=dimensionSizeArray.length-1; i>=0; i--) {
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
	public Set<Integer> getNeighborForDim(int distance, int dimension, int... indices) {
		Set<Integer> set = new HashSet<Integer>();
		int index = calcOneDim(indices);
		set.add(index);
		Set<Integer> resultSet = getNeighborForDim(distance, dimension, set, indices);
		resultSet.remove(index);
		return resultSet;
	}
	
	public Set<Integer> getNeighborForDim(int distance, int dimension, Set<Integer> ignoreValues, int... indices) {
		if (dimension < 0 || dimension > dimensionSizeArray.length-1) {
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
    		if (negativeIndex >= 0 && negativeIndex < multiDimArray.length) {
    			if (ignore && ignoreValues.contains(negativeIndex) == false || ignore == false) {
    				list.add(negativeIndex); // negative
    			}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		} try {
    		tmpIndices[dim] = indices[dim]+1;
    		int positiveIndex = calcOneDim(tmpIndices);
    		if (positiveIndex >= 0 && positiveIndex < multiDimArray.length) {
    			if (ignore && ignoreValues.contains(positiveIndex) == false || ignore == false) {
    				list.add(positiveIndex); // negative
    			}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		}
    	
    	return list;
	}

}
