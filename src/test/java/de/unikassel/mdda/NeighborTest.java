package de.unikassel.mdda;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class NeighborTest {
	
	@Test
	public void testZeroNeighbor2Dim() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3);
		a.set(66.6, 1,1);
		
		
		Set<Integer> list = a.getNeighborForAllDims(0, 1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		
		a.print();
				
	}
	
	@Test
	public void testNeighbor2Dim() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3);
		a.set(0.0, 1,1);
		
		
		Set<Integer> list = a.getNeighborForAllDims(1, 1,1);
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
		
		String[][][] v = new String[3][3][3];
		v[1][1][1] = "ceter";
		v[1][1][0] = "x-1";
		v[1][1][2] = "x+1";
		v[1][0][1] = "y-1";
		v[1][2][1] = "y+1";
		v[0][1][1] = "z-1";
		v[2][1][1] = "z+1";
		
				
		// get neighbors with distance 1 for indizes (1,1,1)
		Set<Integer> list = a.getNeighborForAllDims(1, 1,1,1);
		for (int i : list) {
			int[] multiIndices = a.getMultiDimIndices(i);
			a.setPseudo((String)getMultiDimFromObject(v, multiIndices), i);
		}
		
		assertEquals("center", a.get(1,1,1));
		assertEquals("x-1", a.get(1,1,0));
		assertEquals("x+1", a.get(1,1,2));
		assertEquals("y-1", a.get(1,0,1));
		assertEquals("y+1", a.get(1,2,1));
		assertEquals("z-1", a.get(0,1,1));
		assertEquals("z+1", a.get(2,1,1));
		
	}
	
	private Object getMultiDimFromObject(Object o, int[] i) {
		if (i.length == 1) {
			Object[] a = (Object[]) o;
			return a[i[0]];
		} else if (i.length == 2) {
			Object[][] a = (Object[][]) o;
			return a[i[0]][i[1]];
		} else if (i.length == 3) {
			Object[][][] a = (Object[][][]) o;
			return a[i[0]][i[1]][i[2]];
		} else if (i.length == 4) {
			Object[][][][] a = (Object[][][][]) o;
			return a[i[0]][i[1]][i[2]][i[3]];
		} else if (i.length == 5) {
			Object[][][][][] a = (Object[][][][][]) o;
			return a[i[0]][i[1]][i[2]][i[3]][i[4]];
		}
		return null;
	}

	@Test
	public void testNeighborWithDistanceTwo() {
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,3);
		a.set(9.0, 1,1);
		
		/*
		 * 0 - 1 - 2
		 * |   |   |
		 * 3 - 4 - 5
		 * |   |   |
		 * 6 - 7 - 8
		 */
		
		Set<Integer> list = a.getNeighborForAllDims(2, 1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		
		a.print();
		
		assertEquals(Double.valueOf(1), a.get(0,1));
		assertEquals(Double.valueOf(3), a.get(1,0));
		assertEquals(Double.valueOf(5), a.get(1,2));
		assertEquals(Double.valueOf(7), a.get(2,1));
		
		//distance = 2
		
		assertEquals(Double.valueOf(0), a.get(0,0));
		assertEquals(Double.valueOf(2), a.get(0,2));
		assertEquals(Double.valueOf(6), a.get(2,0));
		assertEquals(Double.valueOf(8), a.get(2,2));
		

	}
	
	@Test
	public void testNeighbor3DimBig() {
		
		MDDAPseudo<String> a = new MDDAPseudo<String>(5,5,5);
		a.set("center", 2,2,2);
		
		String[][][] v = new String[5][5][5];
		v[2][2][2] = "center";
		v[2][2][0] = "x-2";
		v[2][2][1] = "x-1";
		v[2][2][3] = "x+1";
		v[2][2][4] = "x+2";
		
		v[2][0][2] = "y-2";
		v[2][1][2] = "y-1";
		v[2][3][2] = "y+1";
		v[2][4][2] = "y+2";
		
		v[0][2][2] = "z-2";
		v[1][2][2] = "z-1";
		v[3][2][2] = "z+1";
		v[4][2][2] = "z+2";
				
		Set<Integer> list = a.getNeighborForAllDims(3, 2,2,2);
		for (int i : list) {
			int[] multiDimIndices = a.getMultiDimIndices(i);
			String element = (String)getMultiDimFromObject(v, multiDimIndices);
			a.setPseudo(element == null ? "corner" : element, i);
		}

		a.print();


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
		
		Set<Integer> list = a.getNeighborForAllDims(1, 1,1,2);
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
		
		Set<Integer> list = a.getNeighborForDim(1,2, 1,1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}

		a.print();
		
		assertEquals(Double.valueOf(12), a.get(1,1,0));
		assertEquals(Double.valueOf(14), a.get(1,1,2));
		
		list = a.getNeighborForDim(1,0, 1,1,1);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		a.print();

		assertEquals(Double.valueOf(4), a.get(0,1,1));
		assertEquals(Double.valueOf(22), a.get(2,1,1));
	}
	
	@Test
	public void testNeighborForSingleDimWithDistanceTwo() {
		
		MDDAPseudo<Double> a = new MDDAPseudo<Double>(3,5);
		
		a.set(Double.valueOf(0), 1,2);
		
		Set<Integer> list = a.getNeighborForDim(1,1, 1,2);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}

		a.print();
		assertEquals(Double.valueOf(6), a.get(1,1));
		assertEquals(Double.valueOf(8), a.get(1,3));
		
		
		list = a.getNeighborForDim(2,1, 1,2);
		for (int i : list) {
			a.setPseudo(Double.valueOf(i), i);
		}
		
		a.print();
		assertEquals(Double.valueOf(5), a.get(1,0));
		assertEquals(Double.valueOf(6), a.get(1,1));
		assertEquals(Double.valueOf(8), a.get(1,3));
		assertEquals(Double.valueOf(9), a.get(1,4));
		
	}
}
