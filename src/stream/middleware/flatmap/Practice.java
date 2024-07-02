package thisisjava.stream.middleware.flatmap;

import java.util.Arrays;

public class Practice {
    public static void main(String[] args) {
        String [] inputs = new String[]{"10 20 30", "40 50"};
        Arrays.<String>stream(inputs)
                .flatMapToInt((input)->{
                    String [] scoreInput = input.split(" ");
                    return Arrays.stream(scoreInput).mapToInt(Integer::parseInt);
                }).forEach(System.out::println);
    }
}
