package com.kelthuzadx.yarrow.test;

public class LirGenTest {
    public static long lirGen1(int n) {
        int a = n;
        int b = a + 1;
        int c = b + 2;
        int d = a + b + c;
        long e = d;
        return e;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            lirGen1(i);
        }
    }
}
