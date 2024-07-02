package thisisjava.modifier.inner;
public class InnerPackage {

    void m1(){
        // Other other = new Other(); // 내부 패키지여도 default 제한자면 접근이 안된다.
    }
}

class DefaultInnerPackage{
    void m2(){

    }
}
