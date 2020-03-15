package com.kelthuzadx.yarrow.test;

public class NewTest {
    public static void yarrow_newObj(int val) {
        for (int i = 0; i < 100; i++) {
            Object obj = null;
            if (val > 0 && val <= 100) {
                obj = new Object();
            } else if (val > 100 && val <= 200) {
                obj = "LDC";
            } else if (val > 200 && val <= 300) {
                obj = new Class[3];
            } else if (val > 300 && val <= 400) {
                obj = new String[]{"aa", "bb", "cc"};
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            yarrow_newObj(i);
        }
    }
}
