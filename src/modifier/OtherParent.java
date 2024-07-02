package thisisjava.modifier;

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