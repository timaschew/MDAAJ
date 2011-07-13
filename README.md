## Getting Started
The standard way to create an array looks like this:

    int[] a = new int[10];
    double[][] b = new double[10][20];
    String[][][][][] c = new String[10][20][5][8][15];
    c[0][0][2][1][0] = "xyz";
    String s = c[0][0][0][0][0];

Creating the same array with MDDAJ:

    MDDAPseudo<Integer> a = new MDDAPseudo<Integer>(10);
    MDDAPseudo<Double> b = new MDDAPseudo<Double>(10,20);
    MDDAPseudo<String> c = new MDDAPseudo<String>(10,20,5,8,15);
    c.set("xyz", 0,0,2,1,0);
    String s = c.get(0,0,0,0,0);

[Wiki][wiki]

  [wiki]: http://github.com/timaschew/MDAAJ/wiki
