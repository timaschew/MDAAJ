package de.unikassel.mdda;

public class MDDAReal<T> extends MDDABasic<T> {
	
	public MDDAReal(int... dimensionSize) {
		this.dimensionSizeArray = dimensionSize;
		this.dimensionSize = dimensionSize.length;
		multiDimArray = init(0);
	}
	
	private Object init(int dim) {
		Object[] tmp = new Object[dimensionSizeArray[dim]];
		for (int j=0; j<tmp.length; j++) {
			if (dim == dimensionSizeArray.length-1) {
				tmp[j] = new Double(0);
			} else {
				tmp[j] = init(dim+1);
			}
		}
		return tmp;
	}
}
