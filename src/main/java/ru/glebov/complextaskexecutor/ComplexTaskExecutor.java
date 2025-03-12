package ru.glebov.complextaskexecutor;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {

    private final int countOfTasks;

    public ComplexTaskExecutor(int countOfTasks) {
        this.countOfTasks = countOfTasks;
    }

    public void executeTasks() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(countOfTasks);

        ExecutorService executorService = Executors.newFixedThreadPool(countOfTasks);

        for (int i = 0; i < countOfTasks; i++) {

            executorService.submit(new ComplexTask(cyclicBarrier));

        }
        executorService.shutdown();
    }
}
