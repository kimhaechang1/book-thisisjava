package thisisjava.thread.sync2;

class Task{

    int value;
    boolean isExist;

    public synchronized void produce(int value) throws InterruptedException {
        if(isExist){
            // 값이 이미 있다면 기다려야함
             wait();
             System.out.println(Thread.currentThread().getName()+": "+Thread.currentThread().getState());
        }
        this.value = value;
        isExist= true;
        notify(); // 다른 스레드를 호출
    }
    public void consume() throws InterruptedException {
        synchronized(this){
            if(!isExist){
                // 값이 없다면 기다려야함
                wait();
            }
            isExist= false;
            System.out.println("consumer");
            notify(); // 다른 스레드를 호출
        }

    }
}

class Producer extends Thread{

    Task task;

    public Producer(){
        setName("producer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.produce(i);
                Thread.sleep(500);
            }catch(InterruptedException e){}
        }
    }
}

class Consumer extends Thread{

    Task task;

    public Consumer(){
        setName("consumer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.consume();
                Thread.sleep(500);
            }catch(InterruptedException e){}
        }
    }
}

public class Practice {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task(); // 공유하는 객체속에 run 메소드가 있음, 각각은 동기화 되어있고 각 담당 스레드가 작업할거임
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.setTask(task);
        consumer.setTask(task);
        producer.start();
        consumer.start();
    }
}
