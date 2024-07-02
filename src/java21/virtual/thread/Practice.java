package thisisjava.java21.virtual.thread;

public class Practice {
    public static void main(String[] args) {
        // 기존 스레드 생성방식 - 익명 구현 객체
        Thread pthread1 = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        // 기존 스레드 생성방식 - 람다
        Thread pthread2 = new Thread(()->{
           //
        });

        Thread platformThread = Thread.ofPlatform()
                .name("안녕")
                .daemon() // 데몬 쓰레드
                .start(()->{});

        // 버 스레드 - startVirtualThread(Runnable task)
        Thread vthread = Thread.startVirtualThread(()->{
            //
        });

        Thread vthread2 = Thread.startVirtualThread(new Runnable() {
            @Override
            public void run() {

            }
        });

        // ofVirtual
        Thread vthread3 = Thread.ofVirtual()
                .name("스레드 이름")
                .start(()->{});
    }
}
