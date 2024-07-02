package thisisjava.class1;

public interface A extends B, C{
    public int aMethod();
    static void staticMethod(){
        System.out.println("A스태틱 메소드");
    }

    @Override
    default void defaultMethod() {
        System.out.println("A에서 오버라이드");
    }
}
