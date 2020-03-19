package com.kelthuzadx.yarrow.test;

public class LVNTest {
    public static int lvn(int n){
        int a = n;
        int b = n + 3;
        int c = b;
        int d = a + 3;
        return d;
    }

    public static void main(String[] args) {
        for (int i=0;i<10000000;i++){
            lvn(i);
        }
    }
}
