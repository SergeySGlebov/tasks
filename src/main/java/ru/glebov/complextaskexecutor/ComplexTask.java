package ru.glebov.complextaskexecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ComplexTask implements Runnable {
    private final CyclicBarrier barrier;

    public ComplexTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    //count to a million
    @Override
    public void run() {
        try {
            int result = 0;
            for (int i = 0; i < 1_000_000; i++) {
                result++;
            }
            barrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
