package de.unikassel.mdda;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MDDACodeGenTest {
	
	@Test
	public void test2Dim() {
		MDDACodeGen<Double> a = MDDACodeGen.createInstance(Double.class, 3,3);
		a.set(1.0, 0,0);
		a.set(2.0, 1,1);
		a.set(3.0, 2,2);
		
		assertEquals(Double.valueOf(1), a.get(0,0));
		assertEquals(Double.valueOf(2), a.get(1,1));
		assertEquals(Double.valueOf(3), a.get(2,2));
		
	}
	
	@Test
	public void test3Dim() {
		MDDACodeGen<Double> a = MDDACodeGen.createInstance(Double.class, 2,2,2);
		a.set(1.0, 0,0,0);
		a.set(2.0, 0,1,0);
		a.set(3.0, 1,0,0);
		a.set(4.0, 1,1,0);
		
		assertEquals(Double.valueOf(1), a.get(0,0,0));
		assertEquals(Double.valueOf(2), a.get(0,1,0));
		assertEquals(Double.valueOf(3), a.get(1,0,0));
		assertEquals(Double.valueOf(4), a.get(1,1,0));
		
	}
	
	@Test
	public void test4Dim() {
		MDDACodeGen<Double> a = MDDACodeGen.createInstance(Double.class, 2,2,2,2);
		a.set(66.0, 0,1,0,1);
		a.set(1.0, 0,0,0,0);
		a.set(2.0, 0,1,0,0);
		a.set(3.0, 1,0,0,0);
		a.set(4.0, 1,1,0,0);

		assertEquals(Double.valueOf(66), a.get(0,1,0,1));
		assertEquals(Double.valueOf(1), a.get(0,0,0,0));
		assertEquals(Double.valueOf(2), a.get(0,1,0,0));
		assertEquals(Double.valueOf(3), a.get(1,0,0,0));
		assertEquals(Double.valueOf(4), a.get(1,1,0,0));
	}
	
	@Test
	public void testString() {
		MDDACodeGen<String> a = MDDACodeGen.createInstance(String.class, 3,3);
		a.set("center", 1,1);
		assertEquals("center",a.get(1,1));
	}

}
