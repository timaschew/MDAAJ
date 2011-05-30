package de.unikassel.mdda;

import org.junit.Test;

public class PrintTest {
	
	@Test
	public void testPrint1() {
		MDDAPseudo<Double> a = new MDDAPseudo(3,3,3);
		a.set(1.0, 0,0,0);
		a.set(2.0, 1,1,1);
		a.set(3.0, 2,2,2);
		a.print();
		a.prettyPrint();
	}

}
