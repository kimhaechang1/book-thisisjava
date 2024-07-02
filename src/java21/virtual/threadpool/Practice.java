package thisisjava.java21.virtual.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Practice {
    public static void main(String[] args) {
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService platformExecutor = Executors.newFixedThreadPool(100);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    long sum = 0;
                    for(int i = 0;i<1000;i++){
                        sum += i;


                    }
//                     블록킹 - timewait
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

            }
        };
        int taskNum = 10000;
        work(taskNum, task, platformExecutor);

        work(taskNum, task, virtualExecutor);
    }
    static void work(int taskNum, Runnable task, ExecutorService executorService){
        long startTime = System.nanoTime();

        try(executorService){
            for(int i = 0;i<taskNum;i++){
                executorService.execute(task);
            }
        }
        long endTime = System.nanoTime();

        long workTime = endTime - startTime;
        System.out.println("작업처리 시간: "+(workTime));
    }
}
