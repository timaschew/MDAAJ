package de.unikassel.mdda;

import static org.junit.Assert.*;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class MDDATest {
	
	@Test
	public void testToMultiDim() {
		Integer[] d = new Integer[] {1,2,3,4,5,6,7,8,9};
		MDDA<Integer> m = new MDDA<Integer>(d, false, 3,3);
	
		int index = 1;
		for (Integer element : m) {
			Assert.assertEquals(Integer.valueOf(index), element);
			index++;
		}
	}
	
	@Test
	public void testForeach() {
		MDDA<Double> a = new MDDA<Double>(3,3);
		Random r = new Random();
				
		for (int i=0; i<a.getArray().length; i++) {
			a.set1D(r.nextDouble(), i);
		}
		Double sum = 0.0;
		for (Double elem : a) {
			sum += elem;
		}
		
		System.out.println(sum);
		
	}
	
	@Test
	public void test2Dim() {
		MDDA<Double> a = new MDDA<Double>(3,3);
		a.set(1.0, 0,0);
		a.set(2.0, 1,1);
		a.set(3.0, 2,2);
		
		assertEquals(Double.valueOf(1), a.get(0,0));
		assertEquals(Double.valueOf(2), a.get(1,1));
		assertEquals(Double.valueOf(3), a.get(2,2));
		
		for (int i=0; i<a.size[0]; i++) {
			for (int j=0; j<a.size[1]; j++) {
				a.set(null, i,j);
			}
		}
		
	}
	
	@Test
	public void test3Dim() {
		MDDA<Double> a = new MDDA<Double>(2,2,2);
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
		MDDA<Double> a = new MDDA<Double>(2,2,2,2);
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
		MDDA<String> a = new MDDA<String>(3,3);
		a.set("center", 1,1);
		assertEquals("center",a.get(1,1));
	}

}
