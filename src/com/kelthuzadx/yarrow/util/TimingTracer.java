package com.kelthuzadx.yarrow.util;


public class TimingTracer implements AutoCloseable {
    private final String name;
    private final long startTimeMillis;

    public TimingTracer(String name) {
        this.name = name;
        this.startTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void close() {
        long endTimeMillis = System.currentTimeMillis();
        Logger.logf("Task {} take about {}s", name, (endTimeMillis - startTimeMillis) / 1000);
    }
}
