## 자바 제네릭

우리가 타입을 지정하기 힘들때에는

주로 모든 타입을 다 허용하거나, 타입을 변수로 두는 생각을 하곤한다.

여기서 타입을 변수로 두어서 사용자가 결정지을수 있도록 파라미터를 제공하는것을 제네릭이라고 한다.

### 제네릭을 안쓰고 할순 없을까?

제네릭을 사용하지 않았을 때, JAVA에서는 Object 클래스를 사용해서 모든 타입을 저장할 수 있다.

왜냐하면 자바에서의 Object클래스는 최상위 클래스이기 때문에 자동변환되어 저장되기 때문이다.

그렇게 저장은 큰 문제가 없지만, 나중에 그 값을 사용하는 입장에서는 모든타입을 instanceof로 검사해야 할 것이다.

그리고 캐스팅해서 꺼내야할 것이다.

일반적으로 모든 타입의 경우를 두고 검사하는건 좀 말도안된다.

### 제네릭 타입

제네릭 타입은 결정되지 않은 타입을 파라미터로 가지는 클래스 혹은 인터페이스를 의미한다.

이러한 제네릭 타입은 <> 부호로 표현되고 그 사이에 타입 파라미터들이 위치할 수 있다.

```java
public class Box <T>{ // 결정되지 않은 타입 파라미터 즉, 제네릭 타입을 클래스명 뒤에 선언할 수 있다.

    private T t;

    // public static T g; // static에는 쓸 수 없다.

    /*public static T method(){}*/

    public T get(){
        // 타입 파라미터를 리턴타입으로 사용한다.
        return this.t;
    }

    public void set(T t){
        this.t = t;
    }
}
public class GenericExample {
    public static void main(String[] args) {
        Box<Integer> box1 = new Box<>(); // T가 Integer가 됨
        box1.set(10);
        int intValue = box1.get(); // 10
        System.out.println(intValue);
        Box<String> box2 = new Box<>(); // T가 String이 됨
        box2.set("김회창")
        String stringValue = box2.get();
        System.out.println(stringValue); // 김회창
    }
}
```

위를 살펴보면 정적 메소드나 정적 필드에서는 사용할 수 없음을 알 수 있다.

이는 정적인 멤버의 특징을 생각하면 알 수 있는데

클래스 내에 정적 멤버와 정적 메소드의 특징은 각 코드나 값이 어떠한 인스턴스에서도 동일하다는 것이다.

따라서, 인스턴스 마다 타입이 결정되는 타입 파라미터는 정적인 멤버에는 사용할 수 없다.

### 제네릭 메소드

이전에 제네릭 타입을 정의내린것은 클래스와 인터페이스 이름 뒤에 타입파라미터가 붙는것을 의미했다.

제네릭 메소드에서 사용되는 타입파라미터는 메소드의 리턴타입과 매게변수 타입에 사용된다.

사용법은 제네릭 타입과 동일하게 <> 부호를 사용하여 그 내부에 타입 파라미터 변수를 사용하고

메소드의 리턴타입 왼쪽에 선언하여 사용한다.

```java
public static <T> Box<T> boxing(T t){
    // 타입 선언부에서 타입 파라미터를 지정할 수 있고 리턴타입 왼쪽에 사용된다.
    // 선언된 타입 파라미터는 리턴타입의 제네릭, 메개변수의 타입 혹은 메개변수의 제네릭으로 사용될 수 있다.
    Box<T> box = new Box<>();
    box.set(t);
    return box;
}
public static <K> K get(Box<K> k1){
    // 메소드 리턴타입으로 사용하고 매게변수의 제네릭으로 사용할 수 있다.
    return k1;
}
```

### 겁나 헷갈린다 좀 요약해보자

제네릭 메소드와 제네릭 타입, 결국 둘다 타입파라미터의 사용 기준점에 따라 독립적으로 작동하게 된다.

클래스명이나 인터페이스명에서 사용되는 제네릭 타입의 타입은 **인스턴스** 마다 결졍된다.

하지만 메소드에 타입파라미터가 붙어있는 제네릭 메소드는 타입이 **메소드 호출**마다 결정된다.

이렇게 이해하니까 가장 편하더라

### 제한된 타입 파라미터

타입은 상속관계로 묶여져 있는 경우가 많으므로 특정 타입 또는 자식 타입들까지만 허용하여 제한 할 필요가 있다.

이럴때 T extends 상위타입 을 사용하면 상위타입과 동일하거나 자식 타입들로만 제한을 할 수 있다.

아래는 타입 파라미터가 메소드에 붙어있는 제네릭 메소드로서 메소드 호출시에 타입이 결정된다.

그리고 그 타입은 Number 클래스와 같거나 그 자식 타입으로만 제한된다.

그리고 타입을 제한하게 되면, 해당 타입이 갖고있는 메소드를 사용할 수 있게 된다.

```java
public static <T extends Number> boolean compare(T t1, T t2){
        // 제네릭 제한이 Number 이므로, Number 타입 메서드호출이 가능하다.
        double d1 = t1.doubleValue();
        double d2 = t2.doubleValue();
        return d1 == d2;
    }

public static <T> boolean compareDiff(T t1, T t2){
    // 제한이 없으므로 사용할 수 있는 메서드가 Object의 메소드로 제한된다.
    return true;
}
```

### 와일드카드 ? 와 상한경계 하한경계

제네릭 타입에는 와일드카드로서 ? 를 사용하면 미확정을 의미한다.

이는 제네릭 타입의 선언부를 제외하고 사용할 수 있다.

이렇게 미확정 타입으로 제네릭 타입이 지정된 인스턴스는 값에 변조를 가할 수 없으며

값을 Read하게 되면 Object 타입이 된다.

즉, ? 와일드카드를 사용하게 되면 불변이 되므로 안정적으로 값을 유지할 때 사용된다.

그리고 제네릭 메소드에서 리턴타입이나 파라미터 타입의 상한선 혹은 하한선을 지정할 수 있다.

상한선으로 지정할 때에는 extends, 하한선으로 지정할 때에는 super 키워드를 사용한다.

### ? extends Number 와 T extends Number 의 차이

그러면 같은 extends 를 사용하지만 ? 일 경우와 타입 파라미터를 줬을 때는 어떤 차이가 있을까

일단 기본적으로 와일드카드 문자는 선언부에선 사용할 수 없다.

왜냐하면 어떤 타입일지 미확정이기 때문이다.

하지만 ? 를 사용한 경우 Number의 모든 하위 혹은 상위 타입을 사용할 수 있다는 의미가 되고

타입 파라미터를 사용한 경우에는 범위를 제한하되, 그 범위내에 하나를 지정한다는 의미가 된다.

### 자동변환과 제네릭

인터페이스 또한 우리가 제네릭 타입을 선언할 수 있다.

그렇게 인터페이스가 제네릭 타입으로 사용되고, 해당하는 타입을 매게변수로 사용하는 경우

자동변환이 적용되어 메소드의 경우 오버라이딩된 메소드가 호출될 수 있다.

```java
public interface A { // 제네릭 타입으로 사용될 인터페이스

    public static final int a = 10;
    public void method();

    default void method2(){
        System.out.println("method2");
    }

}
```

```java
public class AImpl1 implements A{ // A의 구현체 1

    @Override
    public void method() {
        System.out.println("a1에서 재정의된 method1");
    }

    public void a1Method(){
        System.out.println("안녕 A1");
    }
}
```

```java
public class AImpl2 implements A{ // A의 구현체 2

    @Override
    public void method() {
        System.out.println("a2에서 재정의된 method");
    }

    @Override
    public void method2() {
        System.out.println("a2에서 재정의된 method2");
    }
}
```

```java
public class AGeneric <T>{ // 제네릭 타입을 사용하는 클래스
    private T field;

    public AGeneric(T t){
        field = t;
    }

    public T get(){
        return this.field;
    }

}
```

```java
public class Common{ // 매게변수가 제네릭 타입을 사용하는 클래스로 된 메서드
    public static void call(AGeneric<? extends A> generic){
        generic.get().method();
        generic.get().method2();
        System.out.println(generic.get().a);
    }
}
```

```java
public class Main { // 호출부
    public static void main(String[] args) {
        Common.call(new AGeneric<A>(new AImpl2())); // AGeneric 클래스의 타입은 A타입으로 지정하고 자식 클래스 생성자를 할당 (자동변환)
    }
}
```

```java
a2에서 재정의된 method // 오버라이딩 된 메서드 호출
a2에서 재정의된 method2
10
```
