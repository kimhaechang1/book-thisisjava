## 가상 스레드

사실상 21 이전까지는 여러 사용자의 동시적인 요청을 스레드 풀링 기법으로 요청을 처리하였다.

여기서 스레드 풀링이란, 제한된 수의 스레드를 가지고 운용하는것을 의미한다.

자바 17까지는 운영체제가 제공하는 플렛폼 스레드를 래핑했기 때문에, 스레드와 플렛폼 스레드가 1:1로 매핑되었다.

이러한 플렛폼 스레드는 CPU 또는 메모리 사용량이 많이 들기 때문에 수를 제한해서 사용할 수 밖에 없었지만

21에 추가된 가상스레드는 이러한 단점을 극복하기 위해서, 보다 경량화 되고 스레드와 플렛폼 사이의 n:1 로 매핑하게 된다.

가상스레드는 CPU에서 계산을 수행하는 동안만 플렛폼 스레드를 사용한다.

다른말로하자면 파일 입출력이나 네트워킹 등의 수행에서는 가상스레드는 중단되지만, 플렛폼 스레드는 다른 가상스레드의 작업을 처리한다.

### 가상 스레드 풀 생성

가상 스레드는 메모리가 부족하지 않는다면, 무제한으로 사용할 수 있다.

가상 스레드풀을 `Executors`를 사용하여 만들 수 있다.

```java
ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
```

다음 예제는 10000건의 작업을 플렛폼 스레드 100개로 처리하는 것과 가상스레드로 처리하는것의 시간차이를 볼 수 있다.

여기서 중요한 점은 블록킹 I/O 등에 의해 스레드가 블록킹 상태가 되었을 시,

일반 스레드(플렛폼 스레드)의 경우에는 운영체제의 스레드와 직결되기 할당받은 시간만큼을 소비하되, 기다리는동안에 다른 작업이 이뤄지지 않지만

가상 스레드의 경우 이러한 블록킹 I/O가 발생했을시 JVM의 가상스레드만 중단될 뿐 운영체제와 직결되는 스레드는 다른 가상스레드의 작업을 수행한다.

```java
public class Practice {
    public static void main(String[] args) {
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService platformExecutor = Executors.newFixedThreadPool(100);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    long sum = 0;
                    for(int i = 0;i<1000;i++){
                        sum += i;


                    }
                    // 블록킹 - timewait
                    Thread.sleep(10);
                } catch (InterruptedException e) {
               }

            }
        };
        int taskNum = 10000;
        work(taskNum, task, platformExecutor);

        work(taskNum, task, virtualExecutor);
    }
    static void work(int taskNum, Runnable task, ExecutorService executorService){
        long startTime = System.nanoTime();

        try(executorService){
            for(int i = 0;i<taskNum;i++){
                executorService.execute(task);
            }
        }
        long endTime = System.nanoTime();

        long workTime = endTime - startTime;
        System.out.println("작업처리 시간: "+(workTime));
    }
}
```

```
블록킹 안넣고 newFixedThreadPool(10000) 상태

작업처리 시간: 1066973201
작업처리 시간: 459573300
```

```
블록킹(10ms) 넣고 newFixedThreadPool(10000) 상태

작업처리 시간: 1825623800
작업처리 시간: 98247300
```

```
블록킹(10ms) 넣고 newFixedThreadPool(100) 상태

작업처리 시간: 1589151100
작업처리 시간: 93729101
```

```
블록킹 안넣고 newFixedThreadPool(100) 상태

작업처리 시간: 207064400
작업처리 시간: 34785600
```

```
블록킹(100ms) 넣고 newFixedThreadPool(100) 상태

작업처리 시간: 11082850400
작업처리 시간: 181829799
```

진짜 블록킹이 없다하더라도 훨씬 빠르다!

### 가상 스레드 생성

이제 JAVA 21 부터는 가상스레드와 플렛폼 스레드를 선택하여 만들 수 있다.

```java
Thread.startVirtualThread(Runnable task);
Thread.ofVirtual() // builder
```

경량화되고 숫자가 무제한으로 있을 수 있는 가상 스레드는

중첩된 메소드 호출 수가 적고 수명이 짧은 스레드를 사용할 때 사용된다.

주로, JDBC 쿼리 처리 혹은 단일 네트워크 통신 등에 사용될 수 있다.

플렛폼 스레드의 경우 하나의 스레드에 들어가는 비용이 크기 때문에, 호출 스택이 깊은 작업을 처리해야 한다.

특히 서버를 24시간 구동하기 위한 역할을 수행하는 스레드는 플렛폼 스레드여야만 한다.

여기서 개별 리퀘스트에 대해 대응하는 스레드는 경량 스레드인 가상 스레드여도 충분하다.

위에 빌더 패턴으로 가상스레드를 만들 수 있게 된점과 더불어서, 플렛폼 스레드도 `ofPlatform()` 정적 메소드로 만들 수 있다.

```java
Thread.ofPlatform();
```

```java
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
```
