package com.kelthuzadx.yarrow.optimize;

public interface Phase {
    Phase build();

    String name();

    void log();

    class Incrementer {
        static int phaseId = 1;
    }
}
