## 접근 제한자

자바에서는 다양한 제한자가 존재하는데,

그 중에서 접근제한자는 캡슐화를 이루기 위한 도구로서

외부로부터의 공개범위를 결정할 수 있다.

참고로 아무런 접근제한자를 안붙이면 default 접근제한자가 붙게 된다.

<table>
<tr>
    <td>접근 제한자</td>
    <td>적용 대상</td>
    <td>공개 범위</td>
</tr>
<tr>
    <td>public</td>
    <td>class, field, constructor, method</td>
    <td>어디에서나 접근 가능</td>
<tr>
<tr>
    <td>protected</td>
    <td>field, constructor, method</td>
    <td>같은 패키지거나 자식 객체에서만</td>
<tr>
<tr>
    <td>default</td>
    <td>class, field, constructor, method</td>
    <td>같은 패키지</td>
<tr>
<tr>
    <td>private</td>
    <td>field, constructor, method</td>
    <td>클래스 내부에서만 접근가능</td>
<tr>
</table>

실험한 내용들

```java
package other;

public class OtherParent {

    protected void method(){}

    void defaultMethod(){}
}

class Other{
    public void m1(){
        OtherParent op = new OtherParent();
        op.defaultMethod(); // default 메소드는 같은 패키지 내부에서는 접근이 가능하다.
    }
}
```

```java
package type;

public class Parent {
    private void method(){}
    protected void method1(){}
    public void method2(){
        // DefaultInnerPackage di = new DefaultInnerPackage();
        // 내부 패키지에 있더라도, default면 접근이 안된다.

    }
}

class SamePackage{
    private void m1(){
        Parent parent = new Parent();
        parent.method1(); // protected 메소드이지만, 같은 패키지이므로 접근이 가능하다.
    }
}
```

```java
package other.inner;

public class InnerPackage {

    void m1(){
        // Other other = new Other(); // 내부 패키지여도 default 제한자면 접근이 안된다.
    }
}

class DefaultInnerPackage{
    void m2(){

    }
}
```

```java
package type;

import other.OtherParent;

class Child extends OtherParent{
    void childMethod(){
        method(); // 해당 메소드가 protected고 사용장소가 다른 패키지라면, 상속받은 클래스에서 접근이 가능하다.
    }

}
class Child2{
    void method(){
        Child child = new Child(); // default 클래스이므로 같은 패키지에서 접근이 가능하다.
        child.childMethod();  // default 메소드이므로 같은 패키지내에서 접근이 가능하다.
    }
}
public class Practice {

    public static void main(String[] args) {
        Parent parent =new Parent(); // 현재 Parent는 public class 이므로 접근이 자유롭다.
        // parent.method(); // private은 Parent 클래스 내부에서만 접근이 가능하므로 다른 패키지에 다른 클래스에서는 접근이 안된다.
        parent.method1(); // protected는 같은 패키지 및 자식 객체에서 접근이 가능하므로 문제가 없다.
        parent.method2(); // public이므로 접근이 자유롭다.

        OtherParent op = new OtherParent();
        // op.defaultMethod(); OtherParent 클래스는 public이지만, defaultMethod()는 default이므로 다른 패키지에서 접근이 안된다.
        // op.method(); OtherParent 클래스는 public이나, method()는 protected고 다른 패키지이므로 접근이 안된다.
        Child child = new Child();
        child.childMethod();
    }
}
```
