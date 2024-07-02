package thisisjava.collection.sync;

import java.util.concurrent.CopyOnWriteArrayList;

public class Practice {
    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(10); // 초기값 설정

        // 스레드 A
        Thread threadA = new Thread(() -> {
            // 인덱스 0의 값을 12로 수정
            System.out.println(Thread.currentThread().getName()+" -> "+list.get(0));
            list.set(0, list.get(0)+1);
        });

        // 스레드 B
        Thread threadB = new Thread(() -> {
            // 복사본을 만들어서 작업 진행
            System.out.println(Thread.currentThread().getName()+" -> "+list.get(0));
            list.set(0, list.get(0)+2);
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        // 최종 결과 출력
        System.out.println("Final list: " + list);

    }
}
