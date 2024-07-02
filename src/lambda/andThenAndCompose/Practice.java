package thisisjava.lambda.andThenAndCompose;

import java.util.function.Function;

public class Practice {

    public static void main(String[] args) {
        Function<Integer, Integer> fx = (a) -> a + 2;
        Function<Integer, Integer> gx = (a) -> a * 2;
        System.out.println(fx.andThen(gx).apply(10)); // 10 + 2 = 12 * 2 = 24; // g(f(x))
        System.out.println(fx.compose(gx).apply(10)); // 10 * 2 = 20 + 2 = 22; // f(g(x))
    }
}
