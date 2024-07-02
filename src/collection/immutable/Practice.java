package thisisjava.collection.immutable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Practice {

    public static void main(String[] args) {
        // 각 인터페이스에는 of 정적 메소드가 있다.
        List<Integer> list = List.<Integer>of(new Integer[]{10, 20, 30});
//        list.add(10);     // UnsupportedOperationException
//        list.set(0,15); // UnsupportedOperationException
        Map<Integer, Integer> map = Map.<Integer, Integer>of(10,10,20,20);
        Set<Integer> set = Set.<Integer>of(new Integer[]{10,20,30});

        // Arrays.asList는 불변 List 컬렉션을 생성하는 또다른 메소드이다.
        List<Integer> asList = Arrays.<Integer>asList(new Integer[]{10,20,30});
//        asList.add(20); // UnsupportedOperationException
        asList.set(0,190);
        System.out.println(asList); // [190, 20, 30]
    }
}
