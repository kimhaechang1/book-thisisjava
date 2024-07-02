package thisisjava.generic.bounded.practice;

public interface A {

    public static final int a = 10;
    public void method();

    default void method2(){
        System.out.println("method2");
    }

}
