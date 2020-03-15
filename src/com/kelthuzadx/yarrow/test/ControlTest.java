package com.kelthuzadx.yarrow.test;

public class ControlTest {
    static void yarrow_whileLoop(int k) {
        int i = 0;
        while (i < 1000) {
            i++;
            k += i;
        }
    }

    static void yarrow_forLoopWithIf(int k) {
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                k += i;
            } else {
                k += 12;
            }
        }
    }

    static void yarrow_forLoop2(int t) {
        for (int i = 0; i < 1000; i++) {
            for (int k = 0; k < i; k++) {
                t += k;
            }
        }
    }

    static void yarrow_forLoop(int k) {
        for (int i = 0; i < 1000; i++) {
            k += i;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 99999; i++) {
            yarrow_whileLoop(i);
            yarrow_forLoop(i);
            yarrow_forLoop2(i);
            yarrow_forLoopWithIf(i);
        }
    }
}
