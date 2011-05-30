package de.unikassel.mdda;

public interface MDDAInterface<T> {
	
	public T get(int... indizes);
	
	public boolean set(T value, int... indizes);
	
	public boolean fill(T value);
	
	public T[] flatten();
	
	public void print();
	
	public Object getArray();

}
