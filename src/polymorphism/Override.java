package thisisjava.polymorphism;


class Parent{
    void method1(int a, int b, long c){

    }
    void pMethod1(){
        System.out.println("부모 메소드임");
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
