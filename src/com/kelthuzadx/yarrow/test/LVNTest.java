package com.kelthuzadx.yarrow.test;

public class LVNTest {
    private double d1 = 3.14;
    private double d2 = 2.218;
    private long[] arr = new long[]{3,4,5};

    public int lvn(int n){
        int a = n;
        int b = n + 3;
        int d = a + 3; // can be optimized by lvn

        if(n>100){
            int e = a+3;// can not be optimized by lvn
        }

        {  // OK
            double dd1 = d1;
            double dd2 = d1;
            double dd3 = dd1+dd2;
        }

        { // FAIL
            double dd1 = d2;
            d1 = 12;
            double dd2 = d2;
            double dd3 = dd1+dd2;
        }

        return b+d;
    }

    public static void main(String[] args) {
        for (int i=0;i<10000000;i++){
            new LVNTest().lvn(i);
        }
    }
}
