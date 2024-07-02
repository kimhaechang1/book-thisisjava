package thisisjava.thread.threadpool;

import java.util.concurrent.*;

public class Practice {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String [][] mails = new String[1000][2];
        for(int i = 0;i<mails.length;i++){
            mails[i][0] = "메일: "+i;
            mails[i][1] = i+"@ssafy.com";
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i= 0;i<1000;i++){
            final int idx = i;
            Future<String> future = executorService.<String>submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+": "+mails[idx][0]+" 처리 -> ");
                }
            }, "안녕하세요?");
            try{
                System.out.println(future.get());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
//        for(int i = 0;i<1000;i++){
//            final int idx = i;
//            Future<String> future = executorService.submit(new Callable<String>() {
//                @Override
//                public String call() throws Exception {
//                    if(idx == 99) throw new NullPointerException("예외발생 99번");
//                    return Thread.currentThread().getName()+": "+mails[idx][1]+" -> 처리됨";
//                }
//            });
//            try{
//                String get = future.get();
//                System.out.println(get);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }

        executorService.shutdown();

        boolean isTerm = executorService.awaitTermination(3, TimeUnit.SECONDS);
        if(isTerm){
            executorService.close();
        }
    }
}
