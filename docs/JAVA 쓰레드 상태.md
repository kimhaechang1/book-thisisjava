https://d2.naver.com/helloworld/10963

## 쓰레드 상태

쓰레드의 상태는 Thread 클래스의 열거형으로 선언되어있다.

```java
 public enum State {
        /**
         * Thread state for a thread which has not yet started.
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling
         * {@link Object#wait() Object.wait}.
         */
        BLOCKED,

        /**
         * Thread state for a waiting thread.
         * A thread is in the waiting state due to calling one of the
         * following methods:
         * <ul>
         *   <li>{@link Object#wait() Object.wait} with no timeout</li>
         *   <li>{@link #join() Thread.join} with no timeout</li>
         *   <li>{@link LockSupport#park() LockSupport.park}</li>
         * </ul>
         *
         * <p>A thread in the waiting state is waiting for another thread to
         * perform a particular action.
         *
         * For example, a thread that has called {@code Object.wait()}
         * on an object is waiting for another thread to call
         * {@code Object.notify()} or {@code Object.notifyAll()} on
         * that object. A thread that has called {@code Thread.join()}
         * is waiting for a specified thread to terminate.
         */
        WAITING,

        /**
         * Thread state for a waiting thread with a specified waiting time.
         * A thread is in the timed waiting state due to calling one of
         * the following methods with a specified positive waiting time:
         * <ul>
         *   <li>{@link #sleep Thread.sleep}</li>
         *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
         *   <li>{@link #join(long) Thread.join} with timeout</li>
         *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
         *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
         * </ul>
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         */
        TERMINATED;
}
```

### NEW

스레드가 만들어지고 아직 시작안한 상태

### RUNNABLE

실행가능한 스레드의 상태를 의미함

실행가능한 상태의 스레드는 JVM위에서 실행되고 있다.

하지만 OS로부터 프로세서와 같은 다른 자원을 위해 기다리곤 한다.

즉, NEW 다음 상태로 RUNNABLE상태가 되는데, 이건 자기 자신 차례가 될 때 까지 기다리고 있게 된다.

### BLOCKED

모니토 락을 얻기위해 기다리고 있는 블락된 스레드

블락된 상태에 있는 하나의 스레드는 sychronized 블락 혹은 메서드에 진입하기 위한 모니토 락을 얻기위해 기다린다.

### WAITING

Object.wait()을 호출하거나 Thread.join()을 호출한 스레드가 가지는 상태로서

다른 스레드가 특정 작업을 수행하기를 기다리게 된다.

예를들어서 wati()을 호출한 스레드는 notify() 혹은 notifyAll()을 다른스레드가 호출할때 까지 기다린다.

Thread.join()의 경우는 지정된 스레드가 종료되기를 기다린다.

### TIMED_WAITING

특정시간동안 대기하는 상태의 스레드

WAITING과의 차이는 특정 시간동안 대기하면 생기는 상태이다.

Thread.sleep(long milis), Object.wait(long timeout), Thread.join(long milis)를 호출하면 된다.

### TERMINATED

스레드 실행이 완전 종료된 스레드

### 스레드 상태 변화보기

```java
public class Memory {

    public int memory;

    public void blockWrite(int memory){
        System.out.println(Thread.currentThread().getName()+": "+Thread.currentThread().getState());
        synchronized (this){ // 접근하는 인스턴스를 잠금한다.
            this.memory = memory;
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            System.out.println(Thread.currentThread().getName()+": "+this.memory);
        }
    }
}

public class Task1 extends Thread{

    Memory memory;

    public Task1(String name){
        setName(name);
    }

    public void setMemory(Memory memory){
        this.memory = memory;
    }

    @Override
    public void run() {
        memory.blockWrite(100);
    }
}

public class Task2 extends Thread{

    Memory memory;

    public Task2(String name){
        setName(name);
    }

    public void setMemory(Memory memory){
        this.memory = memory;
    }

    @Override
    public void run() {
        memory.blockWrite(50);
    }
}


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
            Thread.sleep(1000);
        }
    }
}
```

```
task1-status: RUNNABLE // task1 RUNNABLE 상태
task2-status: BLOCKED // task2 락을 획득하지 못하여 BLOCKED
task1-: RUNNABLE // task1 락 획득한 채로 동기화 메서드 진입
task1-status: TIMED_WAITING // task1 Thread.sleep() 만남
task2-status: BLOCKED // task2는 아직 락 못 얻음
task1-status: TIMED_WAITING
task2-status: BLOCKED
task1-status: TIMED_WAITING
task2-status: BLOCKED
task1-: 100
task2-: RUNNABLE // task2가 락을 획득하여 RUNNABLE 상태로 동기화 메소드 진입
task1-status: TERMINATED // task1가 종료됨
task2-status: TIMED_WAITING // Thread.sleep()을 만남
task1-status: TERMINATED
task2-status: TIMED_WAITING
task1-status: TERMINATED
task2-status: TIMED_WAITING
task1-status: TERMINATED
task2-status: TIMED_WAITING
task2-: 50
```

### yield로 다른 스레드에게 현재 CPU를 양보하기

Thread의 정적메서드 yield() 는 현재 스레드가 먹고있는 자원을 자신과 우선순위가 같거나 높은 스레드에게 양보한다.

그래서 다음과 같은 코드가 있다.

```java
public class Work extends Thread{
    public boolean flag;

    public Work(String name){
        setName(name);
    }

    @Override
    public void run() {
        for(int i= 0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+": "+i);
            // Thread.yield();
        }
    }
}

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
```

위의 코드상태로 그대로 실행하면 A가 작동할때도 있고 B가 작동할 때도 있고 뒤죽박죽이지만

주석을 해제하고 실행하면 정확하게 번갈아가면서 실행한다.

이는 스케줄러에게 하나를 출력한 후 스케쥴러에게 yield()메소드를 통해 자원을 다른 스레드에게 양보하겠다가 되어서

스케쥴러가 그 콜에 응하면 반대쪽 스레드가 바로 실행이 된다.
