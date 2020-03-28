package com.kelthuzadx.yarrow.test;

public class LirGenTest {
    public static int lirGen1(int n) {
        int a = n;
        int b = a + 1;
        int c = b + 2;
        int d = a + b + c;
        long e = d;
        float f = e;
        double g = f;
        short h = (short) g;
        byte i = (byte) h;
        char j = (char) i;
        int k = j;
        k <<= 1;
        k >>>= 3;
        k >>= 3;
        k &= 12;
        k |= 123;
        k ^= 342;
        k &= n;
        k = -12;
        k = -b;
        Object o = new Object();
        o = new int[25];
        return k;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            lirGen1(i);
        }
    }
}
