https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%97%B4%EA%B1%B0%ED%98%95Enum-%ED%83%80%EC%9E%85-%EB%AC%B8%EB%B2%95-%ED%99%9C%EC%9A%A9-%EC%A0%95%EB%A6%AC

https://effectiveprogramming.tistory.com/entry/enum%EC%9D%98-%ED%99%9C%EC%9A%A9%EB%B2%95

## Enum

`enum`은 특정한 상수 집합을 정의하기 위한 것이다.

그리고 추가적인 인스턴스 변수를 가질 수 있으며

인스턴스 변수를 열거상수에서 바로 초기화가 가능하다.

### Enum의 몇가지 특징

`enum` 을 컴파일하면 `final`이기 때문에 더이상 상속받지 못한다.

`enum`은 컴파일하면 `java.lang.Enum`을 상속받는다.

`enum`에서 선언한 열거상수는 컴파일 하면 모두 자동으로 `public final static enum {열거 명}` 이 붙게된다.

만약 `enum` 열거체에 `abstract` 메소드를 선언 하거나 인터페이스를 구현하는 구현체로 된다면

열거체가 보유한 열거상수들에게서 반드시 구현해야 한다.

상수의 의미를 가지기 때문에, 같은 의미를 보유하고 있는 여러 인스턴스를 가지고 있으면 안된다.

따라서 미리 `enum`인스턴스들을 열거 상수란 명칭으로 생성해놓고

해당 인스턴스들을 가져다가 사용 할 뿐이다.

따라서 `public`과 `protected` 접근 제한자를 붙일 수 없다.

### 기본적인 사용법

```java
public enum Week { // 열거체 이름

    // 열거 상수 정의
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6)
    ;

    public final int index;
    public final String name;

    Week(int index) {
        this.index = index;
        name = "";
    }

    public int getIndex(){
        return this.index;
    }
}
```

### Enum의 확장

`enum`을 단순한 상수값을 넘어서 상수 메소드로도 이용이 가능하다.

결국 상수마다 다른 역할을 하는 메소드를 갖는것으로, 상수 메소드 구현을 위해서 사용할 수 있다.

```java
public enum Week { // 열거체 이름

    // 열거 상수 정의
    MONDAY{
        @Override
        String getHello() {
            return null;
        }
    },
    TUESDAY{
        @Override
        String getHello() {
            return null;
        }
    }
    ;

    abstract String getHello();

}
```
