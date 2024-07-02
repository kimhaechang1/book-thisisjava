package thisisjava.stream.pr;

import java.util.stream.IntStream;

public class Practice {
    public static void main(String[] args) {
//        int [] arr = new int[] {10,20,30};
//        List<Integer> list = Arrays.asList(10, 20, 30);
//        Stream<Integer> listStream = list.thisisjava.stream();
//        IntStream arrStream = Arrays.thisisjava.stream(arr);
//        listStream.forEach(e -> System.out.println("listStream: "+e));
//        arrStream.forEach(e -> System.out.println("arrStream: "+e));
        IntStream range = IntStream.range(1, 10);
        IntStream rangeClosed = IntStream.rangeClosed(1, 10);
        System.out.println("rangeStream: "+range.sum()); // 45
        System.out.println("rangeClosedStream: "+rangeClosed.sum()); // 55
    }
}
