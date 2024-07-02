package thisisjava.inherit;

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



