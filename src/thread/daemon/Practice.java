package thisisjava.thread.daemon;

class Daemon extends Thread{
    @Override
    public void run() {
        try{
            for(int i = 0;i<10000000;i++){
                System.out.println(Thread.currentThread().getName()+" :"+i);
                Thread.sleep(10);
            }
        }catch(InterruptedException e){
            System.out.println("인터럽트 발생!");
        }
        System.out.println("스레드 종료");
    }
}

public class Practice {
    public static void main(String[] args) {
        Daemon daemon = new Daemon();
        daemon.setDaemon(true);
        daemon.start();
        try{
            Thread.sleep(10000);
        }catch(InterruptedException e){

        }
    }
}
