package thisisjava.modifier.type;

import thisisjava.modifier.OtherParent;

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
