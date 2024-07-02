package thisisjava.generic.bounded.practice;

public class Common{
    public static void call(AGeneric<? extends A> generic){
        generic.get().method();
        generic.get().method2();
        System.out.println(generic.get().a);
    }
}
