## 용어 정리

https://en.wikipedia.org/wiki/Processor_(computing)

https://inpa.tistory.com/entry/%F0%9F%91%A9%E2%80%8D%F0%9F%92%BB-multi-process-multi-thread#multi_process

Processor: 종종 메모리나 다른 데이터 스트림과 같은 외부 데이터 자원으로 작업을 수행하는 전자적 구성요소

대충 CPU를 나타내는 목적으로 사용되는 용어기도 함

Core: CPU 내에 각종 연산작업을 수행하는 핵심요소

Multi Processor: 두개 이상의 프로세서를 포함하고 있는 시스템을 말함

Process: 운영체제 위에서 실행중인 프로그램을 의미

프로세스의 구성요소는 보통 코드, 데이터, 스택, 힙으로 구성되어있음

Multi Process: 운영체제가 하나의 응용프로그램에 대해서 동시에 여러 프로세스를 실행할 수 있게 하는 기술

Multi Tasking: 다수의 프로세스가 공용자원을 나누어서 사용하는것
하지만 CPU가 하나인 시스템에서는 엄밀히 따지면 하나의 프로세스만 수행할 수 있다.
여기서 CPU 스케쥴링과 같은 방법으로 각 프로세스마다 CPU와 같은 자원에 접근하는 시간을 분배한다.
분배받은 프로세스들은 순서대로 처리되는데, 현재 작업중인 프로세스의 시간이 종료되어 다른 프로세스로 교체하는 작업을 컨텍스트 스위칭이라고 한다.
이 컨텍스트 스위칭이 빈번하게 일어나면 병렬 연산이 되는것 처러 보인다.

Thread: 하나의 프로세스 내에서 실제로 작업을 수행하는 주체를 의미한다.
프로세스는 운영체제 위에서 실행되는 프로그램이며 코드, 데이터, 스택, 힙 영역이 독립적으로 구축되지만
쓰레드의 경우 스택 영역만 개별적으로 가지고 나머지 영역은 공유한다.

### 멀티 스레드의 개념

위의 용어 개념을 획득한 후 나아가자면

운영체제가 여러 작업을 동시에 처리하는것을 멀티 태스킹이라고 하는데

꼭 멀티 프로세스 뿐만아니라 멀티 쓰레드도 해당된다.

프로세스는 각 프로세스마다 독립적인 자원을 할당받는다

따라서 자식 프로세스가 죽더라도 독립적인 프로세스기 때문에 다른 프로세스에게 영향을 주지 않는다.

하지만 쓰레드의 경우에는 다르다. 공유 자원이 있고, 스택을 독립적으로 가지기 때문에, 만약 자식 쓰레드하나가 죽는다면 다른 쓰레드에 영향을 미친다.

또한 멀티 스레드에서는 작업중인 스레드가 하나라도 남아있다면, 프로세스가 종료되지 않는다.

자바에서는 메인 쓰레드가 반드시 존재하기 때문에 메인 이외의 쓰레드는 직접 생성하면 된다.

### Thread 클래스

자바에서는 기본 모듈내에 쓰레드를 생성하는 클래스인 Thread 클래스와 쓰레드의 작업을 지정해주는 인터페이스인 Runnable 이 있다.

Thread 클래스의 생성자는

```java
 public Thread(Runnable target) {
    this(null, target, "Thread-" + nextThreadNum(), 0);
}
```

이런식으로 Runnable 인터페이스 타입 객체를 인자로 받는다.

target 파라미터에 Runnable 인터페이스의 구현체 객체를 넣어야 하므로

익명 객체를 사용하여 구현체를 넣을 수 있다.

```java
Thread thread = new Thread(new Runnable{
    @Override
    public void run(){

    }
});
```

여기서 Runnable의 run 메소드는 해당 쓰레드가 실행할 작업을 적는다.

그렇게 만든 Thread 객체는 start() 메소드를통해 실행된다.

```java
public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i= 0;i<5;i++){
                    System.out.println("비프");
                }
            }
        });

        thread.start();
        // main 쓰레드가 실행되고, 해당 쓰레드가 start() 메소드를 호출하여
        // 다른 쓰레드가 실행되고 main쓰레드는 아래의 "띵" 반복문을 수행한다.
        for(int i= 0;i<5;i++){
            System.out.println("띵");
            Thread.sleep(50);
        }
    }
}
```

이밖에도 쓰레드를 사용하는 클래스가 다른 클래스를 상속받지 않아서

Thread 클래스 자체를 상속받아도 되는 상황에서는

상속받은 후 run 메서드를 구현하는 방식도 있고

```java
public class Task extends Thread{

    @Override
    public void run() {
        for(int i = 0;i<5;i++){
            System.out.println("비프");
        }
    }
}

public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Task(); // extends 받은 자식 클래스
        thread.start();
        for(int i= 0;i<5;i++){
            System.out.println("띵");
            Thread.sleep(50);
        }
    }
}
```

익명 자식 클래스를 생성해서 별도의 상속받는 클래스를 만들지 않고 사용하는 방법도 있다.

```java
public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(){
            @Override
            public void run() {
                for(int i= 0;i<5;i++){
                    System.out.println("비프");
                }
            }
        }; // 익명 자식 객체
        thread.start();
        for(int i= 0;i<5;i++){
            System.out.println("띵");
            Thread.sleep(50);
        }
    }
}
```

또한 현재 실행중인 쓰레드가 어떤 쓰레드인지 알 수 있다.

만약 현재 코드가 어떤 쓰레드가 실행중인지 알려면 Thread의 정적 메소드 currentThread()를 통해 쓰레드 객체를 얻고
getName() 메소드를 통해 알 수 있다.

```java
public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Thread main = Thread.currentThread();
        System.out.println(main.getName()+" 는 main 쓰레드");
        for(int i= 0;i<5;i++){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    System.out.println(getName()); // 자식 익명 객체이므로 바로 접근가능
                }
            };
            thread.start();
        }
        Thread thread3 = new Thread(){
            @Override
            public void run(){
                System.out.println(getName()+" 스레드 실행");
            }
        };
        thread3.setName("thread3-"); // 스레드 이름 지정
        thread3.start();
    }
}

```

### 쓰레드 상태

쓰레드를 생성하면 NEW 상태가 된다.

그리고 start() 메소드를 실행하면 바로 run() 메소드를 실행하는 것이 아니라

본인 차례가 된다면 run() 메소드를 실행하게 된다.

이러한 **실행까지의 대기상태**를 **RUNNABLE**라고 부른다.

그리고 실행중인 쓰레드는 자신의 run() 메소드가 전부 처리되기 전 CPU 점유시간을 모두 소모하면 다시 RUNNABLE 상태로 돌아가고 다른 스레드가 CPU자원을 얻어 RUNNING 상태가 된다.

이렇게 **하나의 스레드는 RUNNABLE과 RUNNING 상태를 번갈아가면서 run** 메소드의 실행과 중단이 오가다가, 더이상 실행할 문맥이 없다면
종료 상태 즉, **TERMINATED**가 된다.

근데 이러한 실행중인 쓰레드가 갑자기 일시 정지 상태로 진입하기도 한다.

이러한 일시 정지 상태인 쓰레드는 다시 RUNNING 상태로 가기 위해서 RUNNABLE 상태를 거쳐야 한다.

#### 쓰레드 일시정지 시키기

쓰레드를 일시정지 시키는 메소드는 sleep(), join(), wait() 이 있다.

sleep()은 말그대로 해당 쓰레드의 실행을 인자값만큼 실행중단 시킨다.

join()은 다른 스레드가 작업한 결과를 가지고 또다른 스레드가 실행해야 하는 경우에 사용된다.

다음은 join() 메소드를 사용하여 calculator 클래스의 쓰레드가 종료되고 반환된 값을 사용하여 main 쓰레드가 출력하는 예제이다.

```java
public class Task extends Thread{

    public long sum;

    public long getSum(){
        return sum;
    }

    @Override
    public void run() {
        for(int i = 1;i<=5;i++){
            sum += i;
        }
    }
}

public class BeepPrintExample {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        task.start();
        task.join(); // 위의 task 쓰레드가 종료되기까지 기다림
        // 만약 join을 뺀다면 0이 나올 수 있음
        System.out.println(task.sum);
    }
}
```

또한 자신의 스레드를 RUNNABLE로 만들고 다른 스레드를 RUNNING으로 만들 수 있다.

다음과 같은 상황이 있을 수 있다.

ThreadA는 flag값이 true일 경우마다 실행하도록

무한 반복을 돌면서 flag를 체크하고 있다.

ThreadB는 그냥 일반 작업 쓰레드이다.

현재 flag가 false인데 만약 CPU자원을 ThreadA가 차지해버린다면, 쓸모없는 시간이 낭비될 것이다.

이러한 상황에 yield() 메소드를 호출하여 다른 스레드가 실행 상태로 바꾸도록 할 수 있다.

yield() 메소드는 호출한 쓰레드 객체를 RUNNABLE상태로 바꾸고 다른 쓰레드를 RUNNING으로 실행시킨다.

```java
public class Work extends Thread{
    public boolean flag;

    public Work(String name){
        setName(name);
    }

    @Override
    public void run() {
        while(true){
            if(flag){
                System.out.println(getName()+" 작업...");
            }else{
                Thread.yield(); // 현재 쓰레드를 RUNNABLE로 바꾸고 다른 스레드를 RUNNING 시킴
            }
        }
    }
}
```

```java
public class Practice {
    public static void main(String[] args) throws InterruptedException {
        Work workA = new Work("workThreadA");
        Work workB = new Work("workThreadB");
        workA.flag = true;
        workB.flag = true;
        workA.start();
        workB.start();
        // 두개의 쓰레드가 모두 CPU자원을 경쟁하며 RUNNING, RUNNABLE 상태를 왔다갔다함
        Thread.sleep(5000); // 메인 스레드 기준 5초뒤
        workA.flag = false; // workA가 false가 되면서, yield()를 호출하기에 workB 쓰레드만 집중적으로 사용함
        Thread.sleep(10000);
        workA.flag = true; // 다시 번갈아가면서 RUNNING, RUNNABLE이 왔다갔다 함
    }
}
```

### 쓰레드 동기화

쓰레드는 결국 자기가 CPU 자원을 얻는순간 작동하고 작동하다가도

실행 중단 상태에 진입했다가 더 작업할 수도 있다.

만약 두가지 쓰레드가 한가지 자원에 대해서 접근한다고 하면

동기화가 이루어져 있지 않을때, 의도치 않은 결과가 발생할 수도 있다

예를들어서 작업쓰레드 1번이 자신의 작업 결과를 필드변수 memory에 저장하고 2초간 기다린다고 하자

그 사이에 작업쓰레드 2번이 자신의 작업결과를 동일한 필드변수에 memory에 저장한다고 하자

그러면 먼저 실행된 1번 작업쓰레드의 결과값은 지워지게 된다.

이러한 상황을 방지하기 위해서 JAVA에서는 synchronize 키워드를 사용할 수 있다.

해당 키워드를 사용하면 하나의 쓰레드만이 해당 동기화된 자원에 접근할 수 있다.

다른 쓰레드는 해당 쓰레드의 작업이 끝날 때 까지 접근할 수 없다.

synchronize 키워드는 정적 메소드든 인스턴스 메소드든 상관없이 사용할 수 있고

메소드 단위가 아닌 특정 동기화 블록을 생성할 때에도 synchonize를 사용할 수 있다.

```java
public class Memory {

    public int memory;

    public synchronized void write(int memory){
        // synchronize 메소드
        this.memory = memory;
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        System.out.println(Thread.currentThread().getName()+": "+this.memory);
    }

    public void blockWrite(int memory){
        // synchronize 블록
        synchronized (this){ // 접근하는 인스턴스를 잠금한다.
            this.memory = memory;
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            System.out.println(Thread.currentThread().getName()+": "+this.memory);
        }
    }

    public void noneSyncWrite(int memory){
        // 동기화 없이 실행
        this.memory = memory;
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        System.out.println(Thread.currentThread().getName()+": "+this.memory);
    }
}
```

```java
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
        memory.write(100); // 싱크로 나이즈 메소드 호출
    }
}
```

```java
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
        memory.write(50); // 싱크로 나이즈 메소드 호출
    }
}
```

```java
public class Practice {
    public static void main(String[] args) {
        Memory memory = new Memory();
        Task1 task1 = new Task1("task1-");
        Task2 task2 = new Task2("task2-");
        task1.setMemory(memory);
        task2.setMemory(memory); // 같은 자원에 접근
        task1.start();
        task2.start();
    }
}
```

```
task1-: 100
task2-: 50
```

만약 위의 상황에서 noneSyncWrite()를 호출하도록 task1과 task2를 바꾸고 실행하면

```
task1-: 50
task2-: 50
```

으로 task1-의 실행결과가 없어져버린다.

### wait()과 notify() 를 이용한 스레드 제어

같은 공유 객체를 사용하는 두 쓰레드가 있다고 하자

아래가 그 예제인데

```java
class Task{

    int value;
    boolean isExist;

    public synchronized void produce(int value) throws InterruptedException {
        this.value = value;
        isExist= true;
        System.out.println(Thread.currentThread().getName()+": "+this.value);
    }
    public synchronized void consume() throws InterruptedException {
        if(!isExist){
            System.out.println(Thread.currentThread().getName()+": 값없음");
        }else{
            System.out.println(Thread.currentThread().getName()+": "+this.value);
            isExist= false;
        }
    }
}

class Producer extends Thread{

    Task task;

    public Producer(){
        setName("producer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.produce(i);
                Thread.sleep(100);
            }catch(InterruptedException e){}
        }
    }
}

class Consumer extends Thread{

    Task task;

    public Consumer(){
        setName("consumer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.consume();
                Thread.sleep(100);
            }catch(InterruptedException e){}
        }
    }
}

public class Practice {
    public static void main(String[] args) {
        Task task = new Task();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.setTask(task);
        consumer.setTask(task);
        producer.start();
        consumer.start();
    }
}

```

위의 예제를 실행해보면 몇번은 Consummer가 값을 소비하지만
몇번은 Consummer가 먼저 실행되어 값이 없다고 해버린다.

이와같이 같은 공유 객체속 두개의 동기화 메서드를 각 쓰레드가 작동시킬때

두 쓰레드사이에 통신을 통하여 번갈아가면서 작업해야 할 때 wait()과 notify() 가 사용된다.

이제 wait() 과 notify()를 적당히 써서 서로 통신하게 만들어보자

```java
package thread.sync2;

class Task{

    int value;
    boolean isExist;

    public synchronized void produce(int value) throws InterruptedException {
        if(isExist){
            // 값이 이미 있다면 기다려야함
             wait();
        }
        this.value = value;
        isExist= true;
        System.out.println(Thread.currentThread().getName()+": "+this.value);
        notify(); // 다른 스레드를 호출
    }
    public synchronized void consume() throws InterruptedException {
        if(!isExist){
            // 값이 없다면 기다려야함
            wait();
        }
        System.out.println(Thread.currentThread().getName()+": "+this.value);
        isExist= false;
        notify(); // 다른 스레드를 호출
    }
}

class Producer extends Thread{

    Task task;

    public Producer(){
        setName("producer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.produce(i);
                Thread.sleep(100);
            }catch(InterruptedException e){}
        }
    }
}

class Consumer extends Thread{

    Task task;

    public Consumer(){
        setName("consumer-");
    }

    public void setTask(Task task){
        this.task = task;
    }
    @Override
    public void run(){
        for(int i= 0;i<10;i++){
            try{
                task.consume();
                Thread.sleep(100);
            }catch(InterruptedException e){}
        }
    }
}

public class Practice {
    public static void main(String[] args) {
        Task task = new Task();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.setTask(task);
        consumer.setTask(task);
        producer.start();
        consumer.start();
    }
}

```

여기서 중요한점은 Producer 스레드는 반복문을 돌게 될텐데

자신이 목표로하는 값 저장이 끝나면 현재 스레드를 동작을 멈춘다.

실행중단 상태로 만들고나서, 다른스레드를 실행시키도록 notify()를 호출한다.

그러면 Consumer가 값을 확인하고 처리후 본인자신도 더이상 진행되지 않도록 wait()을 걸고 notify()를 통해 실행중단중인 다른 스레드를 실행시킨다.

근데 wait()과 notify()를 만약에 synchronized 메소드 혹은 블록 밖에서 사용하면 어떻게 될까?

```java
public void consume() throws InterruptedException {
    synchronized(this){
        if(!isExist){
            // 값이 없다면 기다려야함
            wait();
        }
        System.out.println(Thread.currentThread().getName()+": "+this.value);
        isExist= false;

    }
    notify(); // 다른 스레드를 호출
}
```

락을 획득하지 않은 체로 notify()를 호출하므로

IllegalMonitorStateException: current thread is not owner

이 발생한다.

쓰레드가 안전하게 락을 획득한 상태가 아니인데 notify()를 호출하였기에 발생하였다.

### 쓰레드의 안전한 종료

쓰레드의 안전한 종료를 위해서 두가지 방법이 있다.

왜 안전한 종료를 얘기하냐면, 원래 stop()이란 메소드가 있는데

이는 deprecated 되었다.

안전하게 자원을 반납하지 못하기 때문에 그랬다고 한다.

#### 조건이용

예를들어서 while 반복문을 통해 무한루프를 돌고있다가

flag값이 false로 바뀌면 쓰레드를 종료하려고 한다면

while의 반복조건을 flag를 넣어두고 쓰레드를 돌리다가

종료시킬때 flag값을 바꿔버리면 동작중인 쓰레드가 run 메소드내에 무한반복 루프를 종료하고

return문을 만나서 종료하게 될 것이다.

#### interrupt() 메소드 이용

쓰레드 객체의 인스턴스 메소드인 interrupt()를 사용하면

InterruptedException이 발생하고 catch() 블록에서 자원을 정리하고 종료할 수 있다.

```java
class Task extends Thread{

    public void run(){
        try{
            while(true){
                Thread.sleep(1); // 일시 정지 상태를 만듬
            }
        }catch(InterruptedException e){
            System.out.println("인터럽트 발생!");
        }finally{
            System.out.println("리소스 정리");
        }
        System.out.println("종료");
    }
}
public class Practice {
    public static void main(String[] args) {
        Task task =new Task();
        task.start();
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }
        task.interrupt(); // 인터럽트 발생
    }
}
```

그런데 위의 상황에는 실행중인 쓰레드가 아닌, 계속해서 실행도중에 중단된 상태를 의미하게 된다.

이게 아니라 실행중인 쓰레드가 interrupt된 상태를 확인하여 종료시킬 수 있다

```java
class Task extends Thread{

    public void run(){
        while(true){
            if(Thread.interrupted()){
                System.out.println("인터럽트 발생!");
                break;
            }
        }
        System.out.println("종료");
    }
}
public class Practice {
    public static void main(String[] args) {
        Task task =new Task();
        task.start();
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }
        task.interrupt(); // 인터럽트 발생
    }
}
```

### 데몬 쓰레드

메몬 스레드 이전에 사용자 스레드라는 개념이 있다.

사용자 스레드는 비 데몬 스레드를 일컫는 말로서

JAVA의 JVM은 모든 사용자 스레드가 종료될때까지 기다렸다가 종료한다.

일단 기본적으로 진입 메소드도 하나의 main 스레드가 잡히고

해당 스레드내에서 만드는 비 데몬 스레드는 모두 사용자 스레드가 된다.

이와 달리 데몬쓰레드는 주 스레드의 작업을 돕는 보조 스레드로서

주 쓰레드가 종료되면 함께 종료된다.

그리고 데몬쓰레드는 우선순위가 낮기 때문에, 만약 데몬쓰레드가 아주 오래동안 작업하게 되어 먼저 비 데몬 쓰레드들의 작업이 끝나게 되면

데몬쓰레드의 작업은 강제로 종료된다.

왜냐하면 주 쓰레드가 종료되면 데몬쓰레드는 종료되기 때문이다.

```java
class Daemon extends Thread{
    @Override
    public void run() {
        try{
            for(int i = 0;i<10000000;i++){
                System.out.println(Thread.currentThread().getName()+" :"+i);
                Thread.sleep(10);
            }
        }catch(InterruptedException e){
            System.out.println("인터럽트 발생!");
        }
        System.out.println("스레드 종료");
    }
}

public class Practice {
    public static void main(String[] args) {
        Daemon daemon = new Daemon();
        daemon.setDaemon(true);
        daemon.start();
        try{
            Thread.sleep(10000);
        }catch(InterruptedException e){

        }
    }
}
```

위의 예제를 실행해보면 JVM이 데몬스레드를 기다리지않고 사용자 스레드인 주 스레드가 10초뒤에 종료된 후 종료하는 모습을 볼 수 있다.
