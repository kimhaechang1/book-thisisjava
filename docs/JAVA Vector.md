https://inpa.tistory.com/entry/JCF-%F0%9F%A7%B1-ArrayList-vs-Vector-%EB%8F%99%EA%B8%B0%ED%99%94-%EC%B0%A8%EC%9D%B4-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0

## Vector에 대한 잘못된 생각

Vector의 기능은 대부분 ArrayList와 동일하다

그치만 눈에띄는 다른점은 Vector의 주요메서드를 살펴보면 하나같이 synchronized 되어있다.

즉, 한번에 하나의 스레드만 해당 메소드를 실행시킬 수 있다는 것이다.

그런데 메소드에 synchronized가 걸려있다는 점은

해당 메소드에서는 동기화를 보장하지만, Vector 객체 자체에 락이 걸리는것은 아니다.

즉, 만약 서로다른 스레드가 add와 remove를 각각한다면 어느 스레드가 먼저 실행할지 모르기 때문에, 문제가 발생할 수 있다.

```java
public class Practice {
    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i<3;i++){
                    vector.add(3);
                }

                for(int i= 0;i<3;i++){
                    System.out.println(Thread.currentThread().getName()+"-> "+vector.get(i));
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i= 0;i<3;i++){
                    System.out.println(Thread.currentThread().getName()+"-> "+vector.remove(0));
                }
            }
        });
        thread1.start();
        thread2.start();

    }
}
```

위 코드를 보면 스레드1과 스레드2가 각각 서로다른 동기화 메서드를 실행한다.

여기서 각 스레드는 실행에 있어서 동기화를 보장받지만

둘 스레드 사이에서 순서는 보장할 수 없다.

따라서 객체자체를 잠금하는것이 위의 상황에서 의도대로 작동할 가능성이 높다.

```java
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
        thread2.start();

    }
}
```
