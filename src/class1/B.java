package thisisjava.class1;

public interface B {
    public void bMethod();

    public static void staticBMethod(){
        System.out.println("B 스태틱 메소드");
    }

    default void defaultMethod(){
        System.out.println("B 디폴트 메소드");
    }

}
