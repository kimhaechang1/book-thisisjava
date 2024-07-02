package thisisjava.thread.yield;

public class Work extends Thread{
    public boolean flag;

    public Work(String name){
        setName(name);
    }

    @Override
    public void run() {
        for(int i= 0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+": "+i);
            Thread.yield();
        }
    }
}
