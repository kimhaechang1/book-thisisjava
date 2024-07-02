package thisisjava.collection;

import java.util.Vector;

public class Practice {
    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (vector){
                    for(int i = 0;i<3;i++){
                        vector.add(3);
                    }

                    for(int i= 0;i<3;i++){
                        System.out.println(Thread.currentThread().getName()+"-> "+vector.get(i));
                    }
                }
            }
        });
        thread1.setName("addAndGet");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (vector){
                    for(int i= 0;i<3;i++){
                        System.out.println(Thread.currentThread().getName()+"-> "+vector.remove(0));
                    }
                }
            }
        });
        thread1.start();
        thread2.setName("remove");
        thread2.start();

    }
}
