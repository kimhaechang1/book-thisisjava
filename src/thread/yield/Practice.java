package thisisjava.thread.yield;

public class Practice {
    public static void main(String[] args) throws InterruptedException {
        Work workA = new Work("workThreadA");
        Work workB = new Work("workThreadB");
        workA.flag = true;
        workB.flag = true;
        workA.start();
        workB.start();
    }
}
