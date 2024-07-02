package thisisjava.generic.bounded.practice;

public class AImpl1 implements A{

    /*@Override
    public void method2() {
        System.out.println("a1에서 재정의된 method2");
    }*/

    @Override
    public void method() {
        System.out.println("a1에서 재정의된 method1");
    }

    public void a1Method(){
        System.out.println("안녕 A1");
    }
}
