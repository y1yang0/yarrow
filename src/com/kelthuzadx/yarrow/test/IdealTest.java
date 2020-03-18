package com.kelthuzadx.yarrow.test;

public class IdealTest {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
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
        for(int i=0;i<100000;i++){
            int val = 10240;
            boolean t = true;
            if(t){
                val++;
            }
        }
    }
}
