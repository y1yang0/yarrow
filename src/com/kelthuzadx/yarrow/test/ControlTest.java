package com.kelthuzadx.yarrow.test;

public class ControlTest {
    static void whileLoop(int k){
        int i=0;
        while(i<1000){
            i++;
            k += i;
        }
    }

    static void forLoop(int k){
        for(int i=0;i<1000;i++){
            k += i;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 99999; i++) {
            forLoop(i);
        }
    }
}
