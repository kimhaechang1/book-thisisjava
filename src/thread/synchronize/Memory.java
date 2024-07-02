package thisisjava.thread.synchronize;

public class Memory {

    public int memory;

    public synchronized void write(int memory){
        this.memory = memory;
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        System.out.println(Thread.currentThread().getName()+": "+this.memory);
    }

    public void blockWrite(int memory){
        synchronized (this){ // 접근하는 인스턴스를 잠금한다.
            System.out.println(Thread.currentThread().getName()+": "+Thread.currentThread().getState());
            this.memory = memory;
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            System.out.println(Thread.currentThread().getName()+": "+this.memory);
        }
    }

    public void noneSyncWrite(int memory){
        this.memory = memory;
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        System.out.println(Thread.currentThread().getName()+": "+this.memory);
    }
}
