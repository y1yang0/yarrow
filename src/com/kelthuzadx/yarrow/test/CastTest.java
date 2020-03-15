package com.kelthuzadx.yarrow.test;

public class CastTest {
    static class A{
        void stuff(){}
    }
    static class B extends A{
        int val;
        B(int val){this.val=val;}

        void stuff(){}
    }

    private static int val = 45;

    public static void typeCast(int val) {
        double d = val+2;
        d *= 2.0;
        long x = (long) d;
        x/=1;
        float f = x;
        f%=1.0;
        int p = (int) f;
        val = p;
    }

    private static void instanceOf(Object obj){
        if(obj instanceof A){
            ((A) obj).stuff();
        }else{
            int code = obj.hashCode();
            code <<= 21;
            code = -code;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 9999998; i++) {
            typeCast(i);
            instanceOf(new B(i));
        }
    }
}
