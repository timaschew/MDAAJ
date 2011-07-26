package de.unikassel.mdda;

import java.util.Set;

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
 * neighbors for index 4 with a distance of 1 are: 3,5,1,7<br/>
 * neighbors for index 4 with a distance of 2 are: 3,5,1,7,0,2,6,8
 *
 * @param <T>
 */
public interface NeighborInterface<T> {
	
	/**
	 * Returns all neighbor indices (in one dim notation) for the origin which can be reached within the given distance
	 * @param distance or steps
	 * @param indices of the origin
	 * @return
	 */
	public Set<Integer> getNeighborForAllDims(int distance, int... indices);
	
	/**
	 * Returns neighbor indices (in one dim notation) for the origin and for the given dimension which can be reached within the given distance
	 * @param distance
	 * @param dimension
	 * @param indices
	 * @return
	 */
	public Set<Integer> getNeighborForDim(int distance, int dimension, int... indices);

}
