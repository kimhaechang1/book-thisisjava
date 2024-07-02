package thisisjava.stream.middleware.endware.reduce;

import java.util.*;
import java.util.stream.IntStream;

public class Practice {
    static class Data{
        int value;
        String id;
        public Data(String id, int value){
            this.id = id;
            this.value = value;
        }
    }
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        IntStream intStream = list.stream().mapToInt(Integer::intValue);
//        System.out.println(intStream.average().getAsDouble()); // Exception in thisisjava.thread "main" java.util.NoSuchElementException: No value present
        OptionalDouble opt = intStream.average();
        opt.ifPresent(a -> System.out.println(a)); // 값이 있을때만 작동하라는 의미
        opt.orElse(11); // 만약 값이 없다면 인자값을 반환하라는 의미
        if(!opt.isPresent()) System.out.println("값이 없어용"); // 있는지 없는지 검사하는 것

        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data("김", 29));
        dataList.add(new Data("회", 20));
        dataList.add(new Data("창", 30));

        System.out.println(dataList.stream().
                mapToInt(data -> data.value).
                reduce(0, (v1, v2)-> v1+v2));

    }
}
