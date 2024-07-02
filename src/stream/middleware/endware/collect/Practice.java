package thisisjava.stream.middleware.endware.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Practice {
    static class Data{
        String name;
        int idx;
        int value;
        public Data(String n,int idx, int v){
            this.idx = idx;
            name = n;
            value = v;
        }
    }
    public static void main(String[] args) {
        List<Data> list = new ArrayList<>();
        list.add(new Data("김회창", 1,10));
        list.add(new Data("김김김", 1,20));
        list.add(new Data("최최최", 2,30));
        list.add(new Data("최고", 2,40));
//        list.add(new Data("김회창", 30));
        // name이 key이고 value가 value가 되는 Map으로 변환하고 싶다
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(
                (obj)-> obj.name, // 그런데 name이 Comparable이 구현되어있는 클래스인 String으로서 동일한 key가 발생한다.
                // Exception in thisisjava.thread "main" java.lang.IllegalStateException: Duplicate key 김회창 (attempted merging values 10 and 30)
                // 따라서 저기 3번째 데이터를 주석하고 실행하면 정상실행 된다.
                (obj)-> obj.value
        ));
        for(Map.Entry<String, Integer> entry: map.entrySet()){
            System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
        }
        Map<Integer, Double> grouping = list.stream().collect(
                Collectors.groupingBy(
                        (data)-> data.idx,
                        Collectors.averagingDouble(data -> data.value)
                )
        );
        for(Map.Entry<Integer, Double> entry: grouping.entrySet()){
            System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
        }
    }
}
