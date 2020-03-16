package com.kelthuzadx.yarrow.test;

public class FieldTest {
    private final Object special;
    private Object field1;
    private int field2;
    private Object[] arr = new Object[2];

    public FieldTest() {
        special = new Object();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999998; i++) {
            new FieldTest().yarrow_field(i);
        }
    }

    public double yarrow_field(int val) {
        String s = String.valueOf(val);
        s += field2;
        field1 = s;

        arr[0] = s;
        arr[1] = arr[0];
        double returnVal = field2 + 1;
        return returnVal;
    }
}
