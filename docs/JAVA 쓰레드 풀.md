### 스레드 풀

이렇게 병렬처리를 위해 쓰레드를 사용하는것은 좋다.

하지만 개수 제한없이 무한정 쓰레드를 생산한다면 CPU사용률이 올라가서 프로그램 성능이 매우 저하될 것이다.

따라서 스레드 폭증을 안정적으로 유지하려면 쓰레드 풀을 사용하는 것이 좋다.

쓰레드 풀은 작업 처리에 사용되는 작업 스레드를 일정개수로 유지시켜놓고

활성 스레드 개수와, 최대 스레드개수 등을 설정한다.

그래서 작업 큐에 들어오는 스레드들을 각 작업 스레드들이 큐에서 꺼내서 작업하고

반환값이 있을경우 반환시켜준다.

이러한 스레드 풀을 생성하는데에는 Executors의 두가지 정적 메소드와 ThreadPoolExecutor 객체가 있다.

정적 메소드는 newCachedThreadPool()과 newFixedThreadPool(int nThreads) 로 나뉘는데

전자는 작업개수마다 새 스레드를 만들어서 작업하고 60초간 작업이 없다면 종료시킨다.

후자는 코어 스레드수를 정해 둘 수 있으며, 초기에는 0개의 스레드가 있엇다가, 작업요청이 들어오면 생성자로 넘긴 값만큼의 스레드가 늘어난다.

늘어난 스레드는 코어 스레드로서, 작업이 끝나서 유휴상태에 돌입하였다고 해도 종료되지않는다.

ThreadPoolExecutor 객체는 초기 스레드 수, 코어 스레드 수, 유휴 상태 지속시간과 그 단위 그리고 작업큐의 종류를 선택할 수 있다.

쓰레드풀의 스레드를 종료시키는데에는 두가지 방법이 있다.

하나는 shutdown이고 다른 하나는 큐에 남아있는 작업들을 List로 꺼내서 보는 shutdownNow가 된다.

마지막으로 작업을 정의하는데 필요한 인터페이스는 Callable과 Runnable이 있다

Callable은 종료뒤 리턴값을 받아와야할 때 쓰이며,

리턴값은 Future 인터페이스로 받아올 수 있다.

### newCachedThreadPool()

newCachedThreadPool은 Executors의 정적 메소드이다.

까보면 다음과 같다.

```java
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```

즉, 코어 스레드수는 0개, 최대 스레드수는 2^31-1, 60초간의 유휴시간과 동기화 큐를 사용하는 스레드풀이 만들어진다.

결국, 스레드가 필요하면 계속 스레드를 만들어서 요청을 처리하게 된다.

```java
public class Practice {
    public static void main(String[] args) {
        String [][] mails = new String[1000][3];
        for(int i= 0;i<mails.length;i++){
            mails[i][0] = "admin@my.com";
            mails[i][1] = "member"+i+"@my.com";
            mails[i][2] = "신상품 입고";
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0;i<1000;i++){
            final int idx = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+": "+mails[idx][1]+" 처리");
                }
            });
        }
        // 스레드가 필요하면 계속 만들어냄
    }
}
```

콘솔 출력을 보면 필요한만큼 스레드를 계속해서 만드는걸 볼수 있다.

### newFixedThreadPool()

위의 내부코드를 살펴보면

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>());
}
```

로서 넘긴 값만큼 코어 스레드수와 최대 스레드수를 잡고, 유휴시간이 0초로서

놀고있는 스레드여도 죽지 않게 된다.

```java
public class Practice {
    public static void main(String[] args) {
        String [][] mails = new String[1000][3];
        for(int i= 0;i<mails.length;i++){
            mails[i][0] = "admin@my.com";
            mails[i][1] = "member"+i+"@my.com";
            mails[i][2] = "신상품 입고";
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0;i<1000;i++){
            final int idx = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+": "+mails[idx][1]+" 처리");
                }
            });
        }
        // 위의 설정된 10개에서 더이상 늘어나지 않음
    }
}
```

그런데 둘의 차이점이 있다고 하면

하나는 SynchronousQueue 를 사용하고, 다른 하나는 LinkedBlockingQueue 를 사용한다.

SynchronousQueue 의 특징은 내부에 별도의 공간이 없음

그래서 큐에서 take 하려는 스레드도 put하는 스레드가 있을때 까지 대기해야 하고

put하는 스레드도 take 하려는 스레드가 있을때 까지 대기해야 함

LinkedBlockingQueue는 사이즈가 따로 있고 tail 포인터로 갈수록 최신의 데이터가 있음

내부적으로 잠금을 사용하여 여러 스레드가 접근하는걸 막음

더 자세한건 BlockingQueue에 대해서 알아봐야 할듯

### `Callable<T>` 과 `Future<T>`

우리가 흔히 ExecutorService를 사용해서 스레드풀을 생성하고 사용한다.

여기서 해당 스레드풀에 작업을 넣는 과정들이 있는데, 이 과정에서 submit 혹은 execute를 사용한다.

execute의 경우 Runnable 인터페이스 혹은 구현체가 들어갈 수 있고

submit의 경우에는 Runnable과 `Callable<T>` 두가지 모두 가능하다.

그래서 submit으로 작업을 넣었던 것의 결과를 받을때에는 submit메서드를 호출하고

그 결과는 Future라는 객체에 담겨져서 보내어진다.

이것에 대해서 알아보자.

```java
for(int i = 0;i<1000;i++){
    final int idx = i;
    Future<String> future = executorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return Thread.currentThread().getName()+": "+mails[idx][1]+" -> 처리됨";
        }
    });
    System.out.println(future.get());
}
```

`Callable<T>`를 까보면 다음과 같다.

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

call 이라는 메소드는 제네릭 타입 V에 의해 리턴타입이 결정되는 메소드이다.

그리고 위의 설명을 해석해보면

Callable 인터페이스와 Runnable 인터페이스는 둘다 다른 스레드에 의해 실행되기를 기대하는 클래스를 위해 설계된 인터페이스이다.

그러나 Runnable은 반환값을 반환하지 못하며, checked exception을 던질 수 없다.

Executors 클래스는 다른 보통의 형태를 Callable 클래스로 변환하기 위한 유틸리티 메소드를 포함하고 있다.

즉, Runnable과 Callable의 차이로 Callable은 반환값을 반환 받을 수 있고, checked exception을 던질 수 있다는 점

#### checked exception 과 unchecked exception

간단하게만 알아보자면 checked exception은 ClassNotFoundException과 같이 컴파일 단계에서 확인되고, Exception 클래스를 상속받는 exception을 뜻한다.

unchecked exception은 NullPointerException과 같이 실행도중에 발생하는 Exception으로 런타임 환경에서 감지되면 던진다.

```java
for(int i = 0;i<1000;i++){
    final int idx = i;
    Future<String> future = executorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            if(idx == 99) throw new NullPointerException("예외발생 99번");
            return Thread.currentThread().getName()+": "+mails[idx][1]+" -> 처리됨";
        }
    });
    try{
        String get = future.get();
        System.out.println(get);
    }catch(Exception e){
        e.printStackTrace();
    }
}
```

실제로 위의 코드의 콘솔 출력창을 보다보면

```
java.util.concurrent.ExecutionException: java.lang.NullPointerException: 예외발생 99번
	at java.base/java.util.concurrent.FutureTask.report(FutureTask.java:122)
	at java.base/java.util.concurrent.FutureTask.get(FutureTask.java:191)
	at thread.threadpool.Practice.main(Practice.java:23)
Caused by: java.lang.NullPointerException: 예외발생 99번
	at thread.threadpool.Practice$1.call(Practice.java:18)
	at thread.threadpool.Practice$1.call(Practice.java:15)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
```

중간에 checked exception이 잡히는 모습을 볼 수 있다.

그러면 Callable 과 Runnable은 왜 나눠져 있는걸까

대충 이야기를 검색해서 보니까 Callable은 1.5에 나왔고 Runnable은 1.0에 나왔다고 한다.

근데 Runnable을 만들때 당시에만 해도 결과값 반환이라던지, exception 생각을 그렇게 하진 못하고

나중에가서 Runnable 인터페이스의 변화를 주려하니, 이미 해당 코드를 적극적으로 사용하고있는 대부분의 코드에서 변경점을 초래한다.

따라서 별도로 만들었다고 한다.

### `Future<T> submit()`

submit은 아래와 같이 3종류가 있다.

하나는 Runnable 인터페이스 객체를 넣는경우, 즉 반환값을 받을 필요가 없는 경우이다.

두번째는 Runnable 인터페이스 객체긴한데? 이제 반환값을 두번째 인자로 정해놓기 때문에

반환타입 Future 객체에서 get() 메서드 호출해보면 그 값이 나온다.

세번째는 Callable 객체를 넣는 경우로서, 반환값을 받고싶어 하는 경우이다.

```java
public Future<?> submit(Runnable task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<Void> ftask = newTaskFor(task, null);
    execute(ftask);
    return ftask;
}

/**
 * @throws RejectedExecutionException {@inheritDoc}
 * @throws NullPointerException       {@inheritDoc}
 */
public <T> Future<T> submit(Runnable task, T result) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task, result);
    execute(ftask);
    return ftask;
}

/**
 * @throws RejectedExecutionException {@inheritDoc}
 * @throws NullPointerException       {@inheritDoc}
 */
public <T> Future<T> submit(Callable<T> task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task);
    execute(ftask);
    return ftask;
}
```

저기서 newTaskFor을 알아보면 아래와 같고

```java
protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new FutureTask<T>(runnable, value);
}
protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    return new FutureTask<T>(callable);
}
```

FutureTask로 가보자

```java
public FutureTask(Runnable runnable, V result) {
    this.callable = Executors.callable(runnable, result);
    this.state = NEW;       // ensure visibility of callable
}

public FutureTask(Callable<V> callable) {
    if (callable == null)
        throw new NullPointerException();
    this.callable = callable;
    this.state = NEW;       // ensure visibility of callable
}
```

그렇다 여기서 아마 작업에 대한 하나의 객체를 의미하는것 같다.

Executors.callable()은 Runnable 객체를 Callable 객체로 바꿔주는 역할을 수행한다.

해당 Callable 객체는 아래와 같다.

```java
private static final class RunnableAdapter<T> implements Callable<T> {
    private final Runnable task;
    private final T result;
    RunnableAdapter(Runnable task, T result) {
        this.task = task;
        this.result = result;
    }
    public T call() {
        task.run();
        return result;
    }
    public String toString() {
        return super.toString() + "[Wrapped task = " + task + "]";
    }
}
```

저렇게 작업 하나를 만들게 되는것

그러면 다시 execute 메소드로 넘어가서 확인해보자면

```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    /*
        * Proceed in 3 steps:
        *
        * 1. If fewer than corePoolSize threads are running, try to
        * start a new thread with the given command as its first
        * task.  The call to addWorker atomically checks runState and
        * workerCount, and so prevents false alarms that would add
        * threads when it shouldn't, by returning false.
        *
        * 2. If a task can be successfully queued, then we still need
        * to double-check whether we should have added a thread
        * (because existing ones died since last checking) or that
        * the pool shut down since entry into this method. So we
        * recheck state and if necessary roll back the enqueuing if
        * stopped, or start a new thread if there are none.
        *
        * 3. If we cannot queue task, then we try to add a new
        * thread.  If it fails, we know we are shut down or saturated
        * and so reject the task.
        */
    int c = ctl.get();
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        if (! isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    else if (!addWorker(command, false))
        reject(command);
}
```

코어 풀 사이즈 즉, 유휴상태여도 죽지않는 스레드 수 보다 현재 상태가 적다면

스레드를 하나 만들고나서, 해당 스레드에게 작업 인터페이스를 바로 넘긴다.

여기서 살펴보면, 즉 우리가 submit함수를 호출할 때 마다 execute()가 실행 될 것이고,

execute에 작업 가능한 스레드가 있다면 바로 가져가던가 아니면 작업대기큐에 넣던가 가 이루어진다.

//

Worker에 대해서 추가적으로 적기,

runWorker() 메소드에 대해서 추가적으로 적기
