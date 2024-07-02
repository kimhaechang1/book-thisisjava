package thisisjava.thread.synchronize;

public class Practice {
    public static void main(String[] args) throws InterruptedException {
        Memory memory = new Memory();
        Task1 task1 = new Task1("task1-");
        Task2 task2 = new Task2("task2-");
        task1.setMemory(memory);
        task2.setMemory(memory); // 같은 자원에 접근
        task1.start();
        task2.start();

        while(task1.isAlive() || task2.isAlive()){
            System.out.println("task1-status: "+task1.getState());
            System.out.println("task2-status: "+task2.getState());
            Thread.sleep(500);
        }
    }
}
