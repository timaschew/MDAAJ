package de.unikassel.mdda;

public class MDDANested<T> extends MDDABasic<T> {
	
	public MDDANested(int... dimensionSize) {
		this.size = dimensionSize;
		array = init(0);
	}
	
	private Object init(int dim) {
		Object[] tmp = new Object[size[dim]];
		for (int j=0; j<tmp.length; j++) {
			if (dim == size.length-1) {
				tmp[j] = new Double(0);
			} else {
				tmp[j] = init(dim+1);
			}
		}
		return tmp;
	}
}
