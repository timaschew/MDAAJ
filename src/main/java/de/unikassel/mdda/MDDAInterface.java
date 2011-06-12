package de.unikassel.mdda;

public interface MDDAInterface<T> {
	
	public T get(int... indices);
	
	public boolean set(T value, int... indices);
	
	public boolean fill(T value);
	
	public T[] flatten();
	
	public void print();
	
	public Object getArray();

}
