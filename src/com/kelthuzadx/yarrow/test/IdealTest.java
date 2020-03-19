package com.kelthuzadx.yarrow.test;

public class IdealTest {
    private float f = 12.0f;
    private double[] arr = new double[]{3, 35, 5};

    public static void ideal1(int cond) {
        long v1 = 12;
        long v2 = 324;
        float v3 = 32.f;
        float v4 = 23.f;
        double v5 = 23;
        double v6 = 32;
        if (v1 > v2) {
            v2 += v1;
        }
        if (v3 < v4) {
            v3 += v4;
        }
        if (v5 == v6) {
            v5 += v6;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            ideal1(i);
            ideal2(i);
            // Arithmetic
            int k = 23;
            int q = k + k;
            long d = 100L;
            long p = d + d;
            // Negate
            {
                p = -p;
                q = -q;
                float f = 23.0F;
                double pi = 3.14;
                f = -f;
                pi = -pi;
            }
            // Logic
            {
                int t = k & k;
                t = k | k;
                t = k ^ k;
            }
            // ArrayLen
            {
                int[] arr = new int[3];
                int t = arr.length;
                Object[] arrobj = new Object[56];
                int s = arrobj.length;

                arr = new int[]{2, 4, 5, 6};
                int qq = arr.length;
            }
        }

        {
            int p = 12;
            int val = 1;
            if (p == p) {
                val++;
            }
        }
        for (int i = 0; i < 100000; i++) {
            int val = 10240;
            boolean t = true;
            if (t) {
                val++;
            }
        }
    }

    private static int ideal2(int init) {
        IdealTest idealTest = new IdealTest();
        float ff1 = idealTest.f;
        int b = init;
        int c = init;
        int a = b + c;
        int d = b;
        int e = d + c;
        float ff2 = idealTest.f;
        double dd1 = idealTest.arr[2];
        float ff3 = ff1 + ff2;
        int ret = e + a;
        int index = 2;
        double dd2 = idealTest.arr[index];
        double dd3 = dd1 + dd2;
        return ret + (int) dd3;
    }


}
