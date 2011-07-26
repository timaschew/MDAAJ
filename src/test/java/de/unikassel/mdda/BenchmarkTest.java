package de.unikassel.mdda;

import org.junit.Test;
/*
 * Results:
 * 
 * #1
 * code generated multi dim: 76
 * real nested multi dim: 50
 * pseudo muti dim (one dim): 16
 * 
 * #2
 * code generated multi dim: 42
 * real nested multi dim: 85
 * pseudo muti dim (one dim): 14
 *
 * #3
 * code generated multi dim: 45
 * real nested multi dim: 68
 * pseudo muti dim (one dim): 24
 */
public class BenchmarkTest {
	
	@Test
	public void testBenchmark() {
		// 200^3 = 8000000
		// 300^3 = 27000000
		// 20^5  = 3200000
		int[] dimension = new int[] {20,20,20,20,20};
		
		@SuppressWarnings("unchecked")
		MDDAGenerated<Double> codeGenArray = MDDAGenerated.createInstance(Double.class, dimension);
		long codeGenStart = System.currentTimeMillis();
		codeGenArray.fill(Double.MAX_VALUE);
		long codeGenEnd = System.currentTimeMillis();
		
		MDDANested<Double> realArray = new MDDANested<Double>(dimension);
		long realStart = System.currentTimeMillis();
		realArray.fill(Double.MAX_VALUE);
		long realEnd = System.currentTimeMillis();
		
		MDDA<Double> pseudoArray = new MDDA<Double>(dimension);
		long pseudoStart = System.currentTimeMillis();
		pseudoArray.fill(Double.MAX_VALUE);
		long pseudoEnd = System.currentTimeMillis();
		
		System.out.println("code generated multi dim: "+(codeGenEnd-codeGenStart));
		System.out.println("real nested multi dim: "+(realEnd-realStart));
		System.out.println("pseudo muti dim (one dim): "+(pseudoEnd-pseudoStart));
	}

}
