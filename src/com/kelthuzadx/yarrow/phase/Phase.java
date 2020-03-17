package com.kelthuzadx.yarrow.phase;

public interface Phase {
    Phase build();

    String name();

    void log();

    class Incrementer {
        static int phaseId = 1;
    }
}
