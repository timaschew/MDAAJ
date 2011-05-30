package de.unikassel.mdda;

public interface PseudoInterface<T> {
	
	public T getPseudo(int index);
	
	public boolean setPseudo(T value, int index);

}
