## Getting Started
The standard way to create an array looks like this:

    int[] a = new int[10];
    double[][] b = new double[10][20];
    String[][][][][] c = new String[10][20][5][8][15];
    c[0][0][2][1][0] = "xyz";
    String s = c[0][0][0][0][0];

Creating the same array with MDDAJ:

    MDDA<Integer> a = new MDDA<Integer>(10);
    MDDA<Double> b = new MDDA<Double>(10,20);
    MDDA<String> c = new MDDA<String>(10,20,5,8,15);
    c.set("xyz", 0,0,2,1,0);
    String s = c.get(0,0,0,0,0);

[read more in the Wiki][wiki]

[JavaDoc 0.0.5][javadoc]

  [wiki]: http://github.com/timaschew/MDAAJ/wiki
  [javadoc]: http://thunderwave.de//mdda/0.0.5
