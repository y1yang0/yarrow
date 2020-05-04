package com.kelthuzadx.yarrow.test;

import java.util.ArrayList;

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
        o = new StringBuilder[32];
        int[] p = new int[50];
        k += p.length;
        double cmp1 = 123.4d;
        double cmp2 = n + 32.;
        boolean t = cmp1 == cmp2;
        long cmp3 = 21;
        long cmp4 = n + 32;//89
        t = (cmp4 == cmp3);
        Object multi = new int[2][4][5];
        t = multi instanceof Object[];
        o = new ArrayList<>();
        //t = ((ArrayList) o).add(new Object());
        if (true) {
            k = 12 + 23;
        }
        return k;
    }

    public static int lirGen2(int n) {
        int k = n + 1;
        if (k > 20) {
            k =1;
        } else {
            k = 3;
        }
        return k;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            lirGen2(i);
        }
    }
}
