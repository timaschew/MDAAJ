package de.unikassel.mdda;

import java.util.List;

/**
 * @author anton
 *
 * gridded neighbor structure of a 2 dimensional array
 * <pre>
 * 0 - 1 - 2
 * |   |   |
 * 3 - 4 - 5
 * |   |   |
 * 6 - 7 - 8
 * </pre>
 * neighbors for index 4 with a distance of 1 are: 3,5,1,7
 *
 * @param <T>
 */
public interface NeighborInterface<T> {
	
	public List<Integer> getNeighborForAllDims(int distance, int... indizes);
	
	public List<Integer> getNeighborForDim(int distance, int dimension, int... indizes);

}
