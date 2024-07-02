package thisisjava.stream.middleware.filterAndDistinct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class Data{
    int value;

    public Data(int val){
        value = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return value == data.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Data{" +
                "value=" + value +
                '}';
    }
}

public class Practice {
    public static void main(String[] args) {
        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data(10));
        dataList.add(new Data(10));
        dataList.add(new Data(20));

        Stream<Data> dataStream = dataList.stream();
        Stream<Data> distincted = dataStream.distinct();
        distincted.forEach(d -> System.out.println(d));
    }
}
