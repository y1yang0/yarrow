package com.kelthuzadx.yarrow.test;

public class IdealTest {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            // ArithmeticInstr
            int k = 23;
            int q = k + k;
            long d = 100L;
            long p = d+d;
            // Negate
            p = -p;
            q = -q;
            float f = 23.0F;
            double pi = 3.14;
            f = -f;
            pi = -pi;
            // Logic
            int t = k&k;
            t = k|k;
            t = k^k;


        }
    }
}
