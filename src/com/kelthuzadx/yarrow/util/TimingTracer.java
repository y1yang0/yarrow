package com.kelthuzadx.yarrow.util;


public class TimingTracer implements AutoCloseable {
    private String name;
    private long startTimeMillis;

    public TimingTracer(String name) {
        this.name = name;
        this.startTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void close() {
        long endTimeMillis = System.currentTimeMillis();
        Logger.logf("Task {} take about {}s",name,(endTimeMillis - startTimeMillis) / 1000);
    }
}
