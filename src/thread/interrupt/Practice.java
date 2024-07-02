package thisisjava.thread.interrupt;

class Task extends Thread{

    public void run(){
        while(true){
            if(Thread.interrupted()){
                System.out.println("인터럽트 발생!");
                break;
            }
        }
        System.out.println("종료");
    }
}
public class Practice {
    public static void main(String[] args) {
        Task task =new Task();
        task.start();
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }
        task.interrupt(); // 인터럽트 발생
    }
}
