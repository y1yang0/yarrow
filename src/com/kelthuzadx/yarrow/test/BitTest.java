package com.kelthuzadx.yarrow.test;

public class BitTest {
    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            new BitTest().yarrow_bitShift(i);
        }
    }

    public void yarrow_bitShift(int val) {
        val <<= 1;
        long x = 23;
        val >>= x;
        val |= 1;
        val ^= 23;
        val &= 32;
        val >>>= 5;
    }
}
