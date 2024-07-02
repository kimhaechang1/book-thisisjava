package thisisjava.thread.common;

public class Task extends Thread{

    public long sum;

    public long getSum(){
        return sum;
    }

    @Override
    public void run() {
        for(int i = 1;i<=5;i++){
            sum += i;
        }
    }
}
