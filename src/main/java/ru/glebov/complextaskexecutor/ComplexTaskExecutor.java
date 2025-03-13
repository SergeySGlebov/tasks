package ru.glebov.complextaskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ComplexTaskExecutor {

    private final int countOfTasks;

    public ComplexTaskExecutor(int countOfTasks) {
        this.countOfTasks = countOfTasks;
    }

    public void executeTasks() {
        List<Future<Integer>> futures = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(countOfTasks, () -> {
            int result = 0;
            for (Future<Integer> future : futures) {
                try {
                    result += future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Total result: " + result);
        });

        ExecutorService executorService = Executors.newFixedThreadPool(countOfTasks);

        for (int i = 0; i < countOfTasks; i++) {

            futures.add(executorService.submit(new ComplexTask(cyclicBarrier)));

        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
