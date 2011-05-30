package de.unikassel.mdda;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NeighborTest {
	
	@Test
	public void testNeighbor2Dim() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3);
		a.set(0.0, 1,1);
		
		
		List<Integer> list = a.getNeighborForAllDims(1, 1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		
		assertEquals(Double.valueOf(1), a.get(0,1));
		assertEquals(Double.valueOf(3), a.get(1,0));
		assertEquals(Double.valueOf(5), a.get(1,2));
		assertEquals(Double.valueOf(7), a.get(2,1));
				
	}
	
	@Test
	public void testNeighbor3Dim() {
		
		MDDAPseudo<String> a = new MDDAPseudo<String>(3,3,3);
		a.set("center", 1,1,1);
		
		String[] values = new String[]{"x-1", "x+1", "y-1", "y+1", "z-1", "z+1"};
		
		List<Integer> list = a.getNeighborForAllDims(1, 1,1,1);
		int count = 0;
		for (int i : list) {
			a.setPseudo(values[count++], i);
		}
		
		assertEquals("center", a.get(1,1,1));
		assertEquals("x-1", a.get(1,1,0));
		assertEquals("x+1", a.get(1,1,2));
		assertEquals("y-1", a.get(1,0,1));
		assertEquals("y+1", a.get(1,2,1));
		assertEquals("z-1", a.get(0,1,1));
		assertEquals("z+1", a.get(2,1,1));
		
	}
	
	@Test
	public void testNeighbor3DimBig() {
		
		MDDAPseudo<String> a = new MDDAPseudo<String>(5,5,5);
		a.set("center", 2,2,2);
		
		String[] values = new String[]{"x-1","x+1","y-1","y+1","z-1","z+1",
				"x-2","x+2","y-2","y+2","z-2","z+2"};
		
		List<Integer> list = a.getNeighborForAllDims(2, 2,2,2);
		int count = 0;
		for (int i : list) {
			a.setPseudo(values[count++], i);
		}

		assertEquals("center", a.get(2,2,2));
		assertEquals("x-2", a.get(2,2,0));
		assertEquals("x-1", a.get(2,2,1));
		assertEquals("x+1", a.get(2,2,3));
		assertEquals("x+2", a.get(2,2,4));
		
		assertEquals("y-2", a.get(2,0,2));
		assertEquals("y-1", a.get(2,1,2));
		assertEquals("y+1", a.get(2,3,2));
		assertEquals("y+2", a.get(2,4,2));
		
		assertEquals("z-2", a.get(0,2,2));
		assertEquals("z-1", a.get(1,2,2));
		assertEquals("z+1", a.get(3,2,2));
		assertEquals("z+2", a.get(4,2,2));
	}
	
	@Test
	public void testNeighbor3DimBorder() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3,3);
		a.set(Double.valueOf(0), 1,1,2);
		
		List<Integer> list = a.getNeighborForAllDims(1, 1,1,2);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		
		assertEquals(Double.valueOf(13), a.get(1,1,1));
		// (1,1,3) is out of bounds !
		assertEquals(Double.valueOf(11), a.get(1,0,2));
		assertEquals(Double.valueOf(17), a.get(1,2,2));
		assertEquals(Double.valueOf(5), a.get(0,1,2));
		assertEquals(Double.valueOf(23), a.get(2,1,2));
		
	}
	
	@Test
	public void testNeighborForSingleDim() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3,3);
		
		a.set(Double.valueOf(0), 1,1,1);
		
		List<Integer> list = a.getNeighborForDim(1,2, 1,1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}

		assertEquals(Double.valueOf(12), a.get(1,1,0));
		assertEquals(Double.valueOf(14), a.get(1,1,2));
		
		list = a.getNeighborForDim(1,0, 1,1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		assertEquals(Double.valueOf(4), a.get(0,1,1));
		assertEquals(Double.valueOf(22), a.get(2,1,1));
	}
}
