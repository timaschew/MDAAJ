package de.unikassel.mdda;

import org.junit.Test;

public class BenchmarkTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBenchmark() {
		// 200^3 = 8000000
		// 300^3 = 27000000
		// 20^5  = 3200000
		int[] dimension = new int[] {20,20,20,20,20};
		
		MDDAGenerated<Float> codeGenArray = MDDAGenerated.createInstance(Float.class, dimension);
		long codeGenStart = System.currentTimeMillis();
		codeGenArray.fill(Float.MAX_VALUE);
		long codeGenEnd = System.currentTimeMillis();
		
		MDDANested<Float> realArray = new MDDANested<Float>(dimension);
		long nestedStart = System.currentTimeMillis();
		realArray.fill(Float.MAX_VALUE);
		long nestedEnd = System.currentTimeMillis();
		
		MDDA<Float> pseudoArray = new MDDA<Float>(dimension);
		long mddaStart = System.currentTimeMillis();
		pseudoArray.fill(Float.MAX_VALUE);
		long mddaEnd = System.currentTimeMillis();
		
		System.out.println("MDDAGenerated: "+(codeGenEnd-codeGenStart));
		System.out.println("MDDANested: "+(nestedEnd-nestedStart));
		System.out.println("MDDA: "+(mddaEnd-mddaStart));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBenchmarkWithCreating() {
		// 200^3 = 8000000
		// 300^3 = 27000000
		// 20^5  = 3200000
		int[] dimension = new int[] {20,20,20,20,20};

		long codeGenStart = System.currentTimeMillis();
		MDDAGenerated<Double> codeGenArray = MDDAGenerated.createInstance(Double.class, dimension);
		codeGenArray.fill(Double.MAX_VALUE);
		long codeGenEnd = System.currentTimeMillis();
		
		long nestedStart = System.currentTimeMillis();
		MDDANested<Double> realArray = new MDDANested<Double>(dimension);
		realArray.fill(Double.MAX_VALUE);
		long nestedEnd = System.currentTimeMillis();
		
		long mddaStart = System.currentTimeMillis();
		MDDA<Double> pseudoArray = new MDDA<Double>(dimension);
		pseudoArray.fill(Double.MAX_VALUE);
		long mddaEnd = System.currentTimeMillis();
		
		System.out.println("MDDAGenerated (c): "+(codeGenEnd-codeGenStart));
		System.out.println("MDDANested (c): "+(nestedEnd-nestedStart));
		System.out.println("MDDA (c): "+(mddaEnd-mddaStart));
	}
	
	public static void main (String args[]) {
		BenchmarkTest t12 = new BenchmarkTest();
		t12.testBenchmark();
		t12.testBenchmarkWithCreating();
		System.out.println();
		BenchmarkTest t2 = new BenchmarkTest();
		t2.testBenchmarkWithCreating();
		System.out.println();
		BenchmarkTest t1 = new BenchmarkTest();
		t1.testBenchmark();
		
	
	}

}
