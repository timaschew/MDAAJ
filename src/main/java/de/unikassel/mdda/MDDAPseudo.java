package de.unikassel.mdda;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	
	public T get(int... indizes) {
		int index = calcOneDim(indizes);
		return getPseudo(index);
	}
	
	public boolean set(T value, int... indizes) {
		int index = calcOneDim(indizes);
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
		
		int[] prevIndizes = new int[dimensionSizeArray.length];
		boolean[] diff = new boolean[dimensionSizeArray.length];
		for (int i=0; i<prevIndizes.length; i++) {
			prevIndizes[i] = -1;
		}
		int[] tmpIndizes;
		for (int index=0; index<multiDimArray.length; index++) {
			tmpIndizes = getMultiIndizes(index);
			for (int j=0; j<prevIndizes.length; j++) {
				diff[j] = prevIndizes[j] == tmpIndizes[j];
			}
			printLine(index, tmpIndizes, diff);
			prevIndizes = tmpIndizes;
		}
		System.out.println();
	}
	
	private void printLine(int index, int[] indizes, boolean[] diff) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<diff.length; i++) {
			if (diff[i] == false) {
				sb.append("[");
				sb.append(indizes[i]);
				sb.append("]");
				sb.append("\t");
			} else {
				sb.append("\t");
			}
		}
		sb.append(getPseudo(index));
		System.out.println(sb.toString());
	}
	
	private int[] getMultiIndizes(int index) {
		int[] tmpIndizes = new int[dimensionSizeArray.length];
		int tmp = index;
		for (int i=0; i<dimensionOffset.length-1; i++) {
			tmpIndizes[i] = tmp / dimensionOffset[i]; 
			tmp = tmp % dimensionOffset[i];
		}
		tmpIndizes[dimensionOffset.length-1] = tmp;
		return tmpIndizes;
	}





	private String getPrefix(int dim) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<dim; i++) {
			sb.append("\t");
		}
		return sb.toString();
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
	
	public int calcOneDim(int...indizes) {
		if (indizes.length != dimensionSizeArray.length) {
			throw new IllegalArgumentException("indizes length for wrong dimension: "+indizes.length);
		}
		for (int i=0; i<dimensionSizeArray.length; i++) {
			if (indizes[i] < 0 || indizes[i] > dimensionSizeArray[i]-1) {
				throw new ArrayIndexOutOfBoundsException("index is out of dimension bound: "+indizes[i]);
			}
		}
		int index = 0;
		for (int i=0; i<indizes.length-1; i++) {
			index += dimensionOffset[i] * indizes[i];
		}
		index += indizes[indizes.length-1];
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
    
	public List<Integer> getNeighborForAllDims(int distance, int... indizes) {
		calcOneDim(indizes); // check range
		
		List<Integer> list = new ArrayList<Integer>();
		for (int step=1; step<=distance; step++) {
			for (int i=dimensionSizeArray.length-1; i>=0; i--) {
				list.addAll(getNeighborForAllDims(step, indizes, i));
			}
		}
		return list;
	}
	
	public List<Integer> getNeighborForDim(int distance, int dimension, int... indizes) {
		if (dimension < 0 || dimension > dimensionSizeArray.length-1) {
			throw new ArrayIndexOutOfBoundsException("dimension does not exist: "+dimension);
		}
		calcOneDim(indizes); // check range
		List<Integer> list = new ArrayList<Integer>();
		for (int step=1; step<=distance; step++) {
			list.addAll(getNeighborForAllDims(step, indizes, dimension));

		}
		return list;
	}
	
	private List<Integer> getNeighborForAllDims(int distance, int[] indizes, int dim) {
		
		List<Integer> list = new ArrayList<Integer>();
    	
		int[] tmpIndizes = Arrays.copyOf(indizes, indizes.length);
		
		try {
			tmpIndizes[dim] = indizes[dim]-distance;
    		int negativeIndex = calcOneDim(tmpIndizes);
    		if (negativeIndex >= 0 && negativeIndex < multiDimArray.length) {
				list.add(negativeIndex); // negative
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		} try {
    		tmpIndizes[dim] = indizes[dim]+distance;
    		int positiveIndex = calcOneDim(tmpIndizes);
    		if (positiveIndex >= 0 && positiveIndex < multiDimArray.length) {
				list.add(positiveIndex); // negative
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// no neighbor, because position is at border
		}
    	
    	return list;
	}

}
