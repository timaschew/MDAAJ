package de.unikassel.mdda;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

abstract public class MDDABasic<T> implements MDDAInterface<T> {
	
	/**
	 * Have to be initialzed by the subclass<br>
	 * Its the raw multi dim object
	 */
	protected Object array = null;
	
	/**
	 * A dimension size array.<br>
	 * for example 
	 * <pre>int a[][] = new int[10][15]</pre> have the<br>
	 * dimension size array {10, 15}
	 */
	protected int[] size;
	
	/**
	 * Need for code generation
	 */
	protected int sizeLength;
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#getArray()
	 */
	public Object getArray() {
		return array;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#getSize()
	 */
	public int[] getSize() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#set(java.lang.Object, int[])
	 */
	public boolean set(T value, int... indices) {
		T result = access(array, 0, value, true, indices);
		return value.equals(result);
	}

	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#get(int[])
	 */
	public T get(int... indices) {
		return access(array, 0, null, false, indices);
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#fill(java.lang.Object)
	 */
	public boolean fill(T value) {
		return fillValues(array, 0, value);
	}

	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#print()
	 */
	public void print() {
		print(System.out);
	}
	
	public void print(final OutputStream out) {
		printArray(array, out);
	}
	
	/* (non-Javadoc)
	 * @see de.unikassel.mdda.MDDAInterface#flatten()
	 */
	@SuppressWarnings("unchecked")
	public T[] flatten() {
		int sum = 1;
		for (int s : size) {
			sum *= s;
		}
		Object[] flatArray = new Object[sum];
		flatten(array, (T[]) flatArray, 0, new int[]{0});
		return (T[]) flatArray;
	}
	
	/**
	 * Fills values for the given part of the array using java.util.Arrays.fill(obj, value)
	 * and call it recursive
	 * @param partOfArayDim elements of current dimension
	 * @param position corresponds to the current dimension index
	 * @param setValue value to set
	 * @return
	 */
	private boolean fillValues(Object partOfArayDim, int position, T setValue) {
		if (partOfArayDim == null || partOfArayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException(""+partOfArayDim);
		}
		// if in last dimension, only there you can set values
		if (position == size.length-1) {
			Arrays.fill((Object[]) partOfArayDim, setValue);
			return true;
		}
		for (Object o : (Object[]) partOfArayDim) {
			fillValues(o, position+1, setValue);
		}
		return true;
	}
	

	
	/**
	 * Convert the array into a one dimensional array with recursive calls
	 * @param partOfArrayDim elements of current dimension
	 * @param flat because of the recursive function and void return type this is the resulting one dim array
	 * @param position corresponds to the current dimension index
	 * @param counter index for one dimensional array
	 */
	@SuppressWarnings("unchecked")
	private void flatten(Object partOfArrayDim, T[] flat, int position, int[] counter) {
		if (partOfArrayDim == null || partOfArrayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException(""+partOfArrayDim);
		}
		// if in last dimension, only there you can set values
		if (position == size.length-1) {
			for (Object o : (Object[]) partOfArrayDim) {
				flat[counter[0]++] = (T) o;
			}
			return;
		}
		for (Object o : (Object[]) partOfArrayDim) {
			flatten(o, flat, position+1, counter);
		}

	}

	
	/**
	 * Checks the access for the part of the array, if the indices exist - otherwise an exception will be throw
	 * @param partOfArayDim should be an array - otherwise an exception will be throw
	 * @param position corresponds to the current dimension index
	 * @param setValue value to set if needed
	 * @param indices for the multi dim array
	 * @return
	 * @exception IllegalArgumentException if indices not exist
	 */
	@SuppressWarnings("unchecked")
	private T access(Object partOfArayDim, int position, T setValue, boolean update, int... indices) {
		if (partOfArayDim == null) {
			throw new NullPointerException("array part is null");
		} else if(partOfArayDim.getClass().isArray() == false) {
			throw new IllegalArgumentException("object is not an array: "+partOfArayDim);
		} else if(indices.length != size.length) {
			throw new IllegalArgumentException("indices length for wrong dimension: "+indices.length);
		}
		int index = -1;
		try {
			index = indices[position];
			if (position == indices.length-1) {
				if (update) {
					Array.set(partOfArayDim, index, setValue);
				}
				Object result = Array.get(partOfArayDim, index);
				return (result == null) ? null : (T) result;
			}
			Object element = Array.get(partOfArayDim, index);
			return access(element, position+1, setValue, update, indices);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("index is out of bounds: "+index);
		}
	}
	

    private void printArray(Object partOfArrayDim, OutputStream out) {
    	// stop recursion
        if (partOfArrayDim == null || partOfArrayDim.getClass().isArray() == false) {
        	return;
        }
        
        int length = Array.getLength(partOfArrayDim);
        for (int i=0; i<length; i++) {
            Object element = Array.get(partOfArrayDim, i);
            if (element == null || element.getClass().isArray() == false) {
//                System.out.print(element + " ");
            	try {
					out.write(new String(element+" ").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else {
                printArray(element, out);
            }
        }
        try {
			out.write(new String("\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
