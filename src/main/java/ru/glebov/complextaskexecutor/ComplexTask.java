package ru.glebov.complextaskexecutor;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class ComplexTask implements Callable<Integer> {
    private final CyclicBarrier barrier;
    private final List<Integer> resultsOfTasks;

    public ComplexTask(CyclicBarrier barrier, List<Integer> resultsOfTasks) {
        this.barrier = barrier;
        this.resultsOfTasks = resultsOfTasks;
    }

    //count to a million
    @Override
    public Integer call() {
        try {
            int result = 0;
            for (int i = 0; i < 1_000_000; i++) {
                result++;
            }
            resultsOfTasks.add(result);
            barrier.await();
            return result;
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
