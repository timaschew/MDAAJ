package de.unikassel.mdda;

import java.lang.reflect.Array;
import java.util.Arrays;

abstract public class MDDABasic<T> implements MDDAInterface<T> {
	
	/**
	 * Have to be initialzed by the subclass
	 */
	protected Object multiDimArray = null;
	protected int dimensionSize;
	protected int[] dimensionSizeArray;
	
	
	public Object getArray() {
		return multiDimArray;
	}
	
	public boolean set(T value, int... indizes) {
		T result = access(multiDimArray, 0, value, indizes);
		return value.equals(result);
	}

	public T get(int... indizes) {
		return access(multiDimArray, 0, null, indizes);
	}
	
	public boolean fill(T value) {
		return fillValues(multiDimArray, 0, value);
	}

	private boolean fillValues(Object partOfArayDim, int position, T setValue) {
		if (partOfArayDim == null || partOfArayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException(""+partOfArayDim);
		}
		// if in last dimension, only there you can set values
		if (position == dimensionSize-1) {
			Arrays.fill((Object[]) partOfArayDim, setValue);
			return true;
		}
		for (Object o : (Object[]) partOfArayDim) {
			fillValues(o, position+1, setValue);
		}
		return true;
	}
	
	public T[] flatten() {
		int sum = 1;
		for (int size : dimensionSizeArray) {
			sum *= size;
		}
		Object[] flatArray = new Object[sum];
		flatten(multiDimArray, (T[]) flatArray, 0, new int[]{0});
		return (T[]) flatArray;
	}
	
	private void flatten(Object partOfArrayDim, T[] flat, int position, int[] counter) {
		if (partOfArrayDim == null || partOfArrayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException(""+partOfArrayDim);
		}
		// if in last dimension, only there you can set values
		if (position == dimensionSize-1) {
			for (Object o : (Object[]) partOfArrayDim) {
				flat[counter[0]++] = (T) o;
			}
			return;
		}
		for (Object o : (Object[]) partOfArrayDim) {
			flatten(o, flat, position+1, counter);
		}

	}

	
	private T access(Object partOfArayDim, int position, T setValue, int... indizes) {
		if (partOfArayDim == null) {
			throw new NullPointerException("array part is null");
		} else if(partOfArayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException("object is not an array: "+partOfArayDim);
		} else if(indizes.length != dimensionSize) {
			throw new IllegalArgumentException("indizes length for wrong dimension: "+indizes.length);
		}
		int index = -1;
		try {
			index = indizes[position];
			if (position == indizes.length-1) {
				if (setValue != null){
					Array.set(partOfArayDim, index, setValue);
				}
				Object result = Array.get(partOfArayDim, index);
				return (result == null) ? null : (T) result;
			}
			Object element = Array.get(partOfArayDim, index);
			return access(element, position+1, setValue, indizes);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("index is out of bounds: "+index);
		}
	}
	
	public void print() {
		printArray(multiDimArray);
	}

    private void printArray(Object partOfArrayDim) {
    	// stop recursion
        if (partOfArrayDim == null || partOfArrayDim.getClass().isArray() == false) {
        	return;
        }
        
        int length = Array.getLength(partOfArrayDim);
        for (int i=0; i<length; i++) {
            Object element = Array.get(partOfArrayDim, i);
            if (element == null || element.getClass().isArray() == false) {
                System.out.print(element + " ");
            } else {
                printArray(element);
            }
        }
        System.out.println();
    }
}
