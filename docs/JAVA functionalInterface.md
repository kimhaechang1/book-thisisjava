## 자바 함수형 프로그래밍

함수형 프로그래밍이란?

처리해야할 데이터를 갖고있는 데이터 처리부가

외부로부터 데이터 처리방식을 받아서 유연하게 처리하는 프로그래밍을 뜻한다.

자바에서는 8버전부터 람다 표현식을 사용하여 함수형 프로그래밍을 지원한다.

이러한 람다 표현식은 인터페이스에 대한 익명 구현 객체와 대응되며

익명 구현 객체가 람다식으로 표현가능 하려면

인터페이스 내에 단 하나의 추상 메서드를 가져야 한다.

또한 `@FunctionalInterface` 어노테이션을 활용하여, 해당 인터페이스가 함수형 인터페이스 인지 검사한다.

```java
@FunctionalInterface
public interface A {

    public static void a(){
        // 정적 메서드가 있더라도 추상메서드의 개수가 1개면 상관없음
    }
    default int t(){
        // 디폴트 메서드도 매한가지
        return 0;
    }
    public int calc(int x, int y);
}
```

```java
public class Practice {
    public static void main(String[] args) {
        // 원래라면 action 메서드의 인자로 넣을 구현 객체로, 익명 구현 객체를 만들어서 실행한다.
        action(new A(){;
            @Override
            public int calc(int x, int y) {
                return x+y;
            }
        }, 2, 3);

        // 위와같이 단 하나의 추상메서드만이 존재하는 경우 함수형 인터페이스가 될 수 있다.
        // 따라서 람다식으로 표현할 수 있다.
        action((x, y) -> x+y, 2, 3);
    }
    public static void action(A a, int x, int y){
        System.out.println(a.calc(x, y));
    }
}
```

매개변수가 있는 경우 타입을 명시, var 사용 및 타입 생략이 가능하다.

보통 생략한다고 한다.

### 메소드 참조

메소드 참조는 말그대로 참조 할 메소드의 매게변수와 리턴타입을 알아내서

람다식에서 불필요한 중복 코드를 줄이는걸 목적으로 사용한다.

이러한 메소드참조는 두가지 경우가 있다.

인스턴스 메소드와 정적 메소드 참조

인스턴스 메소드 참조의 경우에는 해당 인스턴스::메소드명 으로 참조한다.

정적 메소드의 경우 클래스명::메소드명으로 참조한다.

마지막 상황으로 매게변수의 메소드 참조의 상황이 있다.

매게변수로 넘어온 인자의 메소드를 호출해서 b를 매게변수로 사용할 경우이다.

```java
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Practice {
    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> getMax = (x, y) -> Math.max(x, y);
        // 위의 함수형 인터페이스는 두가지 Integer 값을 받아서 Integer값을 리턴하는 함수형 인터페이스다
        // 오른쪽의 익명 구현 객체 대신에 쓰인 람다식은 결국 두 인자를 받아서 두인자를 그대로 특정 클래스 혹은 인스턴스의 메소드로 사용한다.
        // 위와 같은 상황일때 정적 메소드 참조를 사용할 수 있다고 한다
        getMax = Math::max;

        Consumer<String> sayHello = (name) -> System.out.println(name);
        // 위의 함수형 인터페이스는 한가지 인자를 받아서 사용하기만 하는 함수형 인터페이스이다.
        // 오른쪽 익명 구현 객체를 살펴보면, 인자를 넘겨받아서 System의 정적 필드 out에 있는 인스턴스 메소드에 그대로 넘겨줄 뿐이다.
        // 저렇게 인스턴스 메소드 참조를 사용할 수 있다.
        sayHello = System.out::println;
        System.out.println(getMax.apply(1, 2));
        sayHello.accept("김회창");

        BiFunction<Integer, Integer, Integer> isBigger = (a, b) -> a.compareTo(b);

        // 동일하게 상황인데, 두 인자를 받아서 두인자를 그대로 사용한다.
        // 이게 뭐 다르게 변형해서 사용하는게 아니라, 두 인자를 그대로 변함없이 사용하고 있다.
        // 이런 a 매게변수가 갖고있는 메소드의 매게변수로 다른 인자 b가 사용되는 경우도 메소드 참조형으로 만들 수 있다.
        // 이럴때에는 클래스명::인스턴스 메소드 를 사용하여 참조형으로 줄일 수 있다
        isBigger = Integer::compareTo;
    }
}
```

### 생성자 참조

람다식이 단순히 생성자를 호출하여 객체만 생성하고있다면 생성자 참조로 바꿀 수 있다.

근데 만약에 람다식으로 표현하기 위한 함수형 인터페이스의 메소드 인자와 일치하는 생성자가 없다면 컴파일 오류가 발생한다.

```java
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class Data{
    int a;
    int b;
    public Data(){

    }
    public Data(int a, int b){
        this.a = a;
        this.b= b;
    }
    public Data(int a){
        this(a, 10);
    }

    @Override
    public String toString() {
        return "Data{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}

class DataBuilder{

    public static Data getDefault(Supplier<Data> consumer){
        return consumer.get();
    }

    public static Data getOneParam(Function<Integer, Data> function){
        return function.apply(10);
    }

    public static Data getTwoParams(BiFunction<Integer, Integer, Data> function){
        return function.apply(2, 3);
    }

    public static Data getWrong(BiFunction<Long, Integer, Data> function){
        return function.apply(10L, 19);
    }


}

public class Practice {
    public static void main(String[] args) {
        // System.out.println(DataBuilder.getDefault(()-> new Data()));
        // 단순히 생성자 호출을 통해 객체를 만들어내는 역할만 수행한다면 생성자 참조로 축약 가능
        System.out.println(DataBuilder.getDefault(Data::new));
        System.out.println(DataBuilder.getOneParam(Data::new));
        System.out.println(DataBuilder.getTwoParams(Data::new));
        // 여기서 중요한점은, 지가 알아서 파라미터에 맞게 생성자를 호출한다.
        // System.out.println(DataBuilder.getWrong(Data::new)); // incompatible types: invalid constructor reference
    }
}
```

## java.util.function 패키지

우리가 사실 함수를 변수에 할당하는거 까지는 이해하겠는데

함수 표현을 람다식으로 표현하고, 해당 람다식을 저장하기 위한 타입이 필요할 것이다.

그럴때마다 매번 새롭게 만들지 않기 위해서 자바에서는 다양한 함수형 인터페이스를 제공한다.

https://inpa.tistory.com/entry/%E2%98%95-%ED%95%A8%EC%88%98%ED%98%95-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4-API

대표적으로 Function, Supplier, Runnable, Consumer, Predicate 등이 있다.

### `Function<T, R>` 의 andThen, compose

위는 T타입을 받아서 R타입을 리턴한다는 함수형 인터페이스이다.

추상 메소드가 하나밖에 없기 때문에 람다식으로 표현이 가능하다.

`andThen`은 어떤 함수형 인터페이스의 결과값을 그대로 또다른 함수형 인터페이스의 인자로 사용할때 사용된다.

`compose`는 `andThen`의 반대로 해석하면 된다. 나중에 제공되는 함수를 먼저 거치고 초창기 함수를 거친다.

```java
public class Practice {

    public static void main(String[] args) {
        Function<Integer, Integer> fx = (a) -> a + 2;
        Function<Integer, Integer> gx = (a) -> a * 2;
        System.out.println(fx.andThen(gx).apply(10)); // 10 + 2 = 12 * 2 = 24;
        System.out.println(fx.compose(gx).apply(10)); // 10 * 2 = 20 + 2 = 22;
    }
}
```

위가 가능한 이유는

먼저 `andThen`을 살펴보자면

```java
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
}
```

위와같이 default 메소드로서 두번째로 실행될 함수형 인터페이스를 리턴한다.

해당 함수형 인터페이스의 파라미터는 R의 하한선을 가지고,

리턴값은 새롭게 정의한 V가 상한선을 가진다.

그리고 다음에 실행될 함수형 인터페이스가 리턴되므로 해당 인터페이스의 메소드 apply를 호출하면 된다.

그러면 첫번째로 정의한 apply가 호출된 후 andThen으로 넣어준 apply가 작동하게 된다.

```java
default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
}
```

반대로 compose의 경우에는 인자로 들어온 함수를 먼저 실행하고 첫번째 함수형 인터페이스를 실행하기 때문에

인자로 들어온 함수형 인터페이스의 반환값이 첫번째 함수형 인터페이스의 인자값과 동일해야 한다.
