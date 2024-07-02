package thisisjava.generic.bounded.practice;

public class Main {
    public static void main(String[] args) {
        Common.call(new AGeneric<A>(new AImpl2()));
    }
}
