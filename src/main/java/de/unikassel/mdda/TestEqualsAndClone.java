package de.unikassel.mdda;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class TestEqualsAndClone {
	
	@Test
	public void testEqualsBinding() {
		Integer[] d = new Integer[] {1,2,3,4,5,6,7,8,9};
		MDDA<Integer> m = new MDDA<Integer>(d, true, 3,3);
		Assert.assertEquals(true, m.equalArray(d));
		
		m.set(-11, 0,0);
		d[1] = -99;
		Assert.assertEquals(true, m.equalArray(d));
	}
	
	@Test
	public void testEqualsNonBinding() {
		Integer[] d = new Integer[] {1,2,3,4,5,6,7,8,9};
		MDDA<Integer> m = new MDDA<Integer>(d, false, 3,3);
		Assert.assertEquals(true, m.equalArray(d));
		
		m.set(-11, 0,0);
		Assert.assertEquals(false, m.equalArray(d));
	}
	
	@Test
	public void testEqualArrayAndEquals() {
		Integer[] d = new Integer[] {1,2,3,4,5,6,7,8,9};
		Integer[] d2 = new Integer[] {1,2,3,4,5,6,7,8,9};
		MDDA<Integer> m = new MDDA<Integer>(d, false, 3,3);
		
		Assert.assertEquals(true, m.equalArray(d));
		Assert.assertEquals(true, m.equalArray(d2));
		Assert.assertEquals(true, Arrays.equals(d, d2));
		
		Assert.assertEquals(false, d.equals(d2));
		
	}
	
	
	@Test
	public void testClone() {
		
		MDDA<Integer> m = new MDDA<Integer>(3,3);
		MDDA<Integer> m2 = new MDDA<Integer>(m);
		
		Assert.assertEquals(true, m.equalArray(m2));
		m.set(-11, 0,0);
		Assert.assertEquals(false, m.equalArray(m2));
	}
	
	@Test
	public void testEqualObjects() {
		MDDA<Integer> m = new MDDA<Integer>(3,3);
		m.fill(99);
		MDDA<Integer> m2 = new MDDA<Integer>(3,3);
		m2.fill(99);

		Assert.assertEquals(true, m.equalArray(m2));
		Assert.assertEquals(false, m.equals(m2));
		
		m2 = m;
		Assert.assertEquals(true, m.equalArray(m2));
		Assert.assertEquals(true, m.equals(m2));
	}
	
	@Test
	public void testOtherImpl() {
		@SuppressWarnings("unchecked")
		MDDAGenerated<Double> codeGenArray = MDDAGenerated.createInstance(Double.class, 3,3,3);
		codeGenArray.fill(99.9);
		
		MDDANested<Double> nested = new MDDANested<Double>(3,3,3);
		nested.fill(99.9);
		
		MDDA<Double> m = new MDDA<Double>(3,3,3);
		m.fill(99.9);
		Assert.assertEquals(true, m.equalArray(codeGenArray));
		Assert.assertEquals(true, m.equalArray(nested));
	}
}
