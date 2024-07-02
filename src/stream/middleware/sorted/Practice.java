package thisisjava.stream.middleware.sorted;

import java.util.Comparator;
import java.util.List;

public class Practice {
    static class Data implements Comparable<Data>{
        // comparable을 사용한 방법
        int value;
        public Data(int value){
            this.value = value;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "value=" + value +
                    '}';
        }

        @Override
        public int compareTo(Data o) {
            return this.value - o.value;
        }
    }
    static class DataCompare implements Comparator<Data>{

        @Override
        public int compare(Data o1, Data o2) {
            return o1.value - o2.value;
        }
    }
    public static void main(String[] args) {
        List<Data> list = List.of(new Data(10),new Data(30),new Data(5), new Data(17), new Data(9));
        // comparator를 사용한 방법들
        list.stream().sorted(new DataCompare()).forEach(System.out::println); // sol1
        list.stream().sorted(new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return o1.value - o2.value;
            }
        }).forEach(System.out::println); // sol2
        list.stream().sorted((o1, o2)-> o1.value - o2.value).forEach(System.out::println); // sol3
    }
}
