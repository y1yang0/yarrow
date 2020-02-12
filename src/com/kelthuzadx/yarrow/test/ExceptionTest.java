package com.kelthuzadx.yarrow.test;

import java.io.IOException;

public class ExceptionTest {
    static void dummy(int k){
        System.out.println(k);
    }

    static void f12(int i) throws Exception {
        if(i==10234){
            try{
                throw new Exception("illegal argument");
            }catch (Exception e){
                int continueWork = e.getMessage().length();
                System.out.println(continueWork);
            }
            throw new Exception("illegal argument");
        }else{
            L1:
            for(int x=i;x<1000;x++){
                for(int p=x;p<1000;p++){
                    if(p==123){
                        break L1;
                    }
                }
            }
            System.out.println("work");
        }
    }
    static void f1(int i){
        try {
            f12(i);
            System.out.println("work");
        } catch (Exception e) {
            if(e.getMessage().length()>=100){
                dummy(5);
            }else{
                dummy(7);
            }
        }
    }

    static void b1(int i){
        int s = i;
        s++;
        try{
            if(s>=500 && s<=900){
                throw new IOException("IO E");
            }else{
                throw new IllegalAccessException("illegal");
            }
        }catch (IOException e){
            s +=2;
        }catch (IllegalAccessException e){
            s +=3;
            s *= 4;
        }
        System.out.println(s);
    }

    static void b2(int i) throws Exception {
        throw new Exception("integer"+i);
    }

    public static void main(String[] args) {
        int sum = 0;
        for (int i = 0; i < 99999; i++) {
            try {
                f1(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(sum);
    }
}
