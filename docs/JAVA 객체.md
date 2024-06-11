## 이것이 자바다 - 객체

### 객체지향과 절차지향

객체지향 프로그래밍이란

프로그래밍 하려는 대상을 **객체라는 하나의 단위**로 보고

**해당 대상의 특성과 행동을 표현하는 것에 중점**을 둔 프로그래밍 방식이다.

절차지향 프로그래밍과의 차이로는

절차지향은 말그대로 프로그래밍 된 코드의 순서에 따라 진행하는 것을 절차지향이라고 표현한다.

그러한 순서는 보통 **함수의 호출순서**를 의미하며

데이터가 여러 함수를 거치면서 변경되거나 사용된다.

결국 절차지향은 **데이터의 흐름**이 중요하고

각 데이터들이 사용되는 함수들의 순서가 중요한 프로그래밍 방법에 해당된다.

아래는 절차지향식으로 학생을 표현한 후 데이터를 업데이트하고 출력하는 과정이다.

```java
public class Practice {
    static class Student{
        int age;
        String name;
    }

    public static void insertStudentData(Student student, int age, String name){
        student.age = age;
        student.name = name;
    }

    public static void printInfo(Student student){
        System.out.println("학생의 이름: "+student.name+" 학생의 나이: "+student.age);
    }

    public static void main(String[] args) {
        Student student = new Student();
        insertStudentData(student, 10, "김회창");
        printInfo(student);
        // 여기서 두 함수의 위치가 바뀌면 문제가 발생한다.
    }
}
```

위를 객체지향적으로 학생이란 객체를 생성하고

학생이란 객체의 행위로서 정의하자.

```java
public class Practice {
    static class Student{
        int age;
        String name;

        public void update(int age, String name){
            this.age = age;
            this.name = name;
        }

        public void printInfo(){
            System.out.println("학생의 이름: "+name+" 학생의 나이: "+age);
        }

    }

    public static void main(String[] args) {
        Student student = new Student();
        student.update(10, "김회창");
        student.printInfo();
        // 두 함수의 순서가 바뀌더라도 문제가 발생하지 않는다.
    }
}
```

### 객체지향의 3가지 특성

객체지향에서 중요한 3가지 특성으로 **캡슐화**, **상속**, **다형성** 이 있다.

먼저 캡슐화는 객체의 **필드와 메소드**를 하나로 묶고 구현내용을 **외부로부터 감추는 것**을 의미한다.

즉, 접근제한자를 사용하여 사용자로부터 필드가 잘못 변조되는 일을 줄이고, 사용자 입장에서는 메소드의 구현부를 몰라도 사용할 수 있다는 점이다.

```java
// Data 클래스 정의하는 사람 입장
// 구현부를 구현하고, 공개범위를 접근제한자를 통해 제한할 수 있다.
public class Data {

    private int privateField;
    void method(){}
    void method2(){}
}
```

```java
// Data 클래스 사용자 입장
// 메소드 구현부는 모른다. 접근제한자에 의해 제한된 필드 혹은 메서드에 접근할 수 없다.
public class Practice {

    public static void main(String[] args) {
        Data data = new Data(); // 데이터 객체 생성
        data.method(); // 데이터의 메소드를 호출할 뿐, 그 내부구현은 알지 못한다.
        data.method2();
        // data.privateField; // 접근제한자 private이 달려있으므로 접근할 수 없다.
    }
}
```

다음으로 **상속**이 있다.

상속은 부모 클래스로부터 자원을 공유하는 특성으로서

자식 객체에서 부모 객체의 자원에 접근이 가능하므로

코드의 유지보수성과 재사용성을 높일 수 있다.

기본적으로 자바에서 클래스사이의 상속은 단일 상속만 지원하고

인터페이스 사이에서의 상속은 다중 상속을 지원한다.

```java
class Child extends Parent{
    void method3(){
        System.out.println("자식 메소드 method3");
    }
}
public class Parent {

    void method(){
        System.out.println("부모 메소드 method");
    }

    void method2(){
        System.out.println("부모 메소드 method2");
    }

    public static void main(String[] args) {
        Child child  = new Child();
        child.method(); // 부모 자원 접근 가능
        child.method2();
    }
}

```

마지막으로 **다형성**이 있다.

다형성은 말뜻 그대로 다양한 형태가 존재할 수 있다는 의미로,

같은 이름을 가진 메소드가 오버라이딩과 오버로딩을 통해 다양한 목적으로 사용할 수 있다는 점이다.

이러한 오버라이딩과 오버로딩에는 각각의 규칙이 있다.

오버로딩은 메소드의 이름과 리턴타입은 같아야 하며, 파라미터의 개수 혹은 타입이 달라야한다.

오버라이딩의 경우 부모 메소드를 재정의 하는것으로

부모가 가진 메서드여야 하고, 함수의 구현부와 접근제한자만 달라질 수 있다.

이 때 접근제한자의 경우 부모의 접근제한자보다 더 좁은 범위를 가질 수 없다.

```java
public class Overload {

    protected void originMethod(int a, int b){}
    // private void originMethod(int a, int b){} 오버로딩 규칙에 어긋난다: 파라미터 개수나 타입이 달라야함
    protected void originMethod(long a, int b){}

    // private int OriginMethod(){} 오버로딩 규칙에 어긋난다: 메소드 리턴타입이 다름
    public void originMethod(){} // 접근 제한자는 달라도 됨
}
```

```java
class Parent{
    void method1(int a, int b, long c){

    }
}
public class Override extends Parent{

    // private void method1(int a, int b, long c){}
    // 오버라이드 규칙에 어긋남: 부모 메소드보다 더 좁은범위의 접근 제한자는 사용할 수 없음

    // void method1(int a, int b){}
    // 오버라이드 규칙에 어긋남: 부모 메소드의 파라미터는 바꿀 수 없음

    //int method1(int a, int b, long c){ return 0; }
    // 오버라이드 규칙에 어긋남: 부모 메소드의 리턴타입을 바꿀 수 없음

    public void method1(int a, int b, long c){
        // 더 넓은 범위로 접근 제한자를 바꿀 수 있음
    }
}
```
