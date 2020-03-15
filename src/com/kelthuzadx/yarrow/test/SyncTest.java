package com.kelthuzadx.yarrow.test;

public class SyncTest {
    private static Object obj = new Object();

    public static void sync(int i) {
        synchronized (obj) {
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            sync(i);
        }
    }
}
