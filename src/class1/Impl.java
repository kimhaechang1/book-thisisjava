package thisisjava.class1;

public class Impl implements A{ // 쉼표로 구분하며, 내부에 있는 모든 추상메서드를 구현해야 한다.

    @Override
    public int aMethod() {
        return 0;
    }
    @Override
    public void bMethod() {
        // A만 구현체로 잡았지만, B를 A가 상속받으므로 B의 추상메소드도 구현해야함
    }
    public static void main(String [] args) throws Exception{
        Impl object = new Impl();
        // Impl.staticBMethod(); // B 인터페이스를 상속받지만, static 메소드이기 때문에 B에서 귀속된 형태
        B.staticBMethod();
        System.out.println("c field: "+object.cValue); // C 인터페이스의 필드값 접근가능
        object.defaultMethod(); // default 메소드 오버라이딩 된 값이 출력
    }
}
