package ru.glebov.forkjoinpool;

import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Integer> {
    private final int from, to;

    FactorialTask(int to) {
        this(1, to);
    }

    FactorialTask(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        int range = to - from;
        if (range == 0) {
            return from;
        } else if (range == 1) {
            return from * to;
        } else {
            int mid = from + range / 2;
            FactorialTask leftTask = new FactorialTask(from, mid);
            leftTask.fork();
            return new FactorialTask(mid + 1, to).compute()
                    * leftTask.join();
        }
    }
}
