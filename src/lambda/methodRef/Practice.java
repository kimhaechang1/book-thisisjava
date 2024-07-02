package thisisjava.lambda.methodRef;

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
