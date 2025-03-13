package ru.glebov.complextaskexecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class ComplexTask implements Callable<Integer> {
    private final CyclicBarrier barrier;

    public ComplexTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    //count to a million
    @Override
    public Integer call() {
        try {
            int result = 0;
            for (int i = 0; i < 1_000_000; i++) {
                result++;
            }
            System.out.println("before barier " + result);
            barrier.await();
            System.out.println("after barier " + result);
            return result;
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
