package de.unikassel.mdda;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparisionTest {
	
	@Test
	public void compareImplementations() {
		
		@SuppressWarnings("unchecked")
		MDDAGenerated<Double> codeGenArray = MDDAGenerated.createInstance(Double.class, 3,3,3);
		codeGenArray.set(1.0, 2,2,2);
		Double[][][] codeGenCast = (Double[][][]) codeGenArray.getArray();
		Double value1 = codeGenCast[2][2][2];
		assertEquals(value1, Double.valueOf(1.0));
		
		MDDANested<Double> nested = new MDDANested<Double>(3,3,3);
		nested.set(1.0, 2,2,2);
		Object[] firstNestedDimRealCast= (Object[]) nested.getArray();
		Object[] secondNestedDim = (Object[]) firstNestedDimRealCast[2];
		Object[] thirdNestedDim = (Object[]) secondNestedDim[2];
		Double index_2_2_2 = (Double) thirdNestedDim[2];
		assertEquals(index_2_2_2, Double.valueOf(1.0));
		
		MDDA<Double> pseudoArray = new MDDA<Double>(3,3,3);
		pseudoArray.set(1.0, 2,2,2);
		Object[] oneDimPseudoCast = (Object[]) pseudoArray.getArray();
		Double value3 = (Double) oneDimPseudoCast[3*3*3-1];// index (2,2,2)
		assertEquals(value3, Double.valueOf(1.0));
		System.out.println();
		
	}

}
