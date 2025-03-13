package ru.glebov.complextaskexecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ComplexTaskExecutor {

    private final int countOfTasks;

    public ComplexTaskExecutor(int countOfTasks) {
        this.countOfTasks = countOfTasks;
    }

    public void executeTasks() {
        List<Integer> resultsInTasks = Collections.synchronizedList(new ArrayList<>());
        List<Future<Integer>> futures = new ArrayList<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(countOfTasks, () -> {
            int result = 0;
            for (int resultOfTask : resultsInTasks) {
                result += resultOfTask;
            }
            System.out.println("Potential result: " + result);
        });

        ExecutorService executorService = Executors.newFixedThreadPool(countOfTasks);

        for (int i = 0; i < countOfTasks; i++) {

            futures.add(executorService.submit(new ComplexTask(cyclicBarrier, resultsInTasks)));

        }

        int result = 0;
        for (Future<Integer> future : futures) {
            try {
                result += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total result: " + result);

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
