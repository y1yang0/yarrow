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
            int a = 32;
            int b = 31;
            k += a+b;
        }
    }

    static void yarrow_complex(int k) {
        int val = 12;
        if (k >= 100) {
            if (k >= 200) {
                if (k >= 400) {
                    val += (val ^ 32);
                    int res = val + 1;
                    double d = res;
                }
            }
        }
        for (int i = val; i >= 0; i--) {
            if (i % 2 == 0) {
                continue;
            }
            if (i + val >= 100) {
                val -= 100;
            } else {
                for (int t = i; t < i + 10; t++) {
                    val += t;
                    switch (t) {
                        case 23:
                            val += 32;
                            break;
                        case 323:
                            val += 23;
                        case 32:
                            val += 3233;
                            break;
                        default:
                            val = 44;
                    }
                }
                break;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 99999; i++) {
            yarrow_whileLoop(i);
            yarrow_forLoop(i);
            yarrow_forLoop2(i);
            yarrow_forLoopWithIf(i);
            yarrow_complex(i);
        }
    }
}
