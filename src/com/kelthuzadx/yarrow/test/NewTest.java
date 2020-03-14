package com.kelthuzadx.yarrow.test;

public class NewTest {
    public static void newObj(int val){
        Object obj = null;
        if(val<=100){
            obj = new Object();
        }else{
            obj = "LDC";
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            newObj(i);
        }
    }
}
