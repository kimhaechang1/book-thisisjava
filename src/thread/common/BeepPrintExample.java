package thisisjava.thread.common;

public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        task.start();
        System.out.println(task.sum);
    }
}
