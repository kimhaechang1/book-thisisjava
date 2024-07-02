package thisisjava.lambda.cast;

interface A{
    public void func1(int a, int b);
}

interface B{
    public void func2(int x, int y);
}

public class Practice {

    public static void main(String[] args) {
        B b =  (x, y) -> System.out.println(x + y);
        A a = (A) b;
        a.func1(2, 3);
    }
}
