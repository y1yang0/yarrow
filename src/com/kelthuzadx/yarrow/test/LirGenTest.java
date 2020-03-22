package com.kelthuzadx.yarrow.test;

public class LirGenTest {
    public static int lirGen1(int n){
        int a = n;
        int b = a+1;
        int c = b+2;
        int d = a+b+c;
        return d;
    }

    public static void main(String[] args) {
        for(int i=0;i<100000;i++){
            lirGen1(i);
        }
    }
}
