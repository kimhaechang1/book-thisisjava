## 스위치 문의 변화

일단 제일 처음 스위치문이 등장하고서, JAVA 7에서 문자열에 대한 처리가 가능해졌다.

그 이후 17에 정식도입되어 람다식이 가능해졌고, 하나의 블록문에서의 `return` 처리를 위한 `yield` 예약어가 추가되었다.

이제 21에서 변화된 내용을 살펴보자.

### swtich 문 내 null 처리 도입

기존에 `null`을 처리하기 위해서는 스위치문 밖에서 분기문을 통해 `null` 체크를 한 후 스위치문으로 진입하도록 했어야 했다.

이제는 `null` 체크를 스위치 문 내부에서 할 수 있음으로, 가독성이 증가했다.

```java
public class Practice {
    public static void main(String[] args) {
//        System.out.println(beforeSwitch(null)); // NullPointerException 발생
        System.out.println(after21(null));
        System.out.println(after21("d"));

    }
    public static String beforeSwitch(String data) throws NullPointerException{
        if(data == null) throw new NullPointerException();
        switch(data){
            case "김회창" -> {
                return "레전드";
            }
            default -> {
                return "default";
            }
        }
    }
    public static String after21(String data){
        String result = switch(data){
            case "김회창" -> "레전드";
            case null, default -> "무슨일이고"; // case null 추가
            // 위와 같이 null 이거나 default로 하고싶다면 앞에 case 키워드와 함께 ,로 구분지어 작성
        };
        return result;
    }
}
```

### switch 문 내 패턴매칭 및 `when` 가드

이제 조건에 중심이 되는 값이 참조변수의 경우

`pattern`을 사용하여 타입 검사를 처리하고, 자동 변환한뒤

해당 변수를 초기화 까지 시킨다.

이러한 패턴 매칭에 있어서 중요한점은 아래로 갈수록 더 넓은 타입의 `case` 패턴타입이 와야 한다는 점이다.

특히나 인터페이스의 경우에도 마찬가지이고, `try-catch`문에서 더 큰 `Exception`클래스를 더 아래로 구현하는 이유와 동일하다.

주의할 점으로 스위치 문 내에 `case`사용에 있어서 패턴을 사용한 경우

다음 `case`로 통과시킬 수 없다. 하지만 자기 자신 바로 다음번째가 `default`의 경우에는 통과시킬 수 있다.

```java
public class Practice {
    public static void main(String[] args) {
        A a = new B();
        before(a);
        after(a);
    }

    static void before(Object obj){
        if(obj instanceof B b){
            b.method();
        }else if(obj instanceof A a){
            a.method();
        }else{
            System.out.println("버그");
        }
    }
    static void after(Object obj){
        switch(obj){
            /*
            case A a -> a.method();
            case B b -> b.method();
            Compile Error 발생
            위와 같이 작성하면 어떤 인스턴스로 만들어도 젤 위에서 걸리므로...
            아래로 내려갈수록 커야함
             */
            case B b -> b.method();
            case A a -> a.method();
            case null, default -> System.out.println("버그");
        }
    }
}
class A{
    public void method(){
        System.out.println("Aclass");
    }
}
class B extends A{
    public void method(){
        System.out.println("B Child Class");
    }
}
```

여기서 `when`를 사용하여 좀더 엄격한 조건을 추가할 수도 있다. -> 가드 사용

```java
public class Practice {
    public static void main(String[] args) {
        // 숫자를 입력하면 등급, 등급을 입력하면 숫자가 나오도록
        // swtich case를 활용하여 표현하라
        System.out.println(notUsed("A"));
        System.out.println(notUsed(90));
        System.out.println(after("A"));
        System.out.println(after(90));
    }
    static String notUsed(Object obj){
        String result = switch(obj){
            case Integer i -> {
                if(i >= 70 && i < 80){
                    yield "C";
                }else if(i >= 80 && i < 90){
                    yield "B";
                }else if(i >= 90 && i <= 100){
                    yield "A";
                }else{
                    yield "D";
                }
            }
            case String s -> {
                if("C".equals(s)){
                    yield "70 ~ 79";
                }else if("B".equals(s)){
                    yield "80 ~ 89";
                }else if("A".equals(s)){
                    yield "90 ~ 100";
                }else{
                    yield "D";
                }
            }
            case null, default -> "bug";
        };
        return result;
    }
    // 가드 사용
    static String after(Object obj){
        String result = switch(obj){
            case Integer i when i >= 70 && i < 80 -> "C";
            case Integer i when i >= 80 && i < 90 -> "B";
            case Integer i when i >= 90 && i <= 100 -> "A";
            case String s when "A".equals(s) -> "90 ~ 100";
            case String s when "B".equals(s) -> "80 ~ 89";
            case String s when "C".equals(s) -> "70 ~ 79";
            case null, default -> "bug";
        };
        return result;
    }
}
```
