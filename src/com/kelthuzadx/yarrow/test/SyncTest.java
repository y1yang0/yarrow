package com.kelthuzadx.yarrow.test;

public class SyncTest {
    private static Object obj = new Object();

    public static void yarrow_sync(int i) {
        synchronized (obj) {
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            yarrow_sync(i);
        }
    }
}
