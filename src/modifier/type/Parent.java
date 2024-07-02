package thisisjava.modifier.type;
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
