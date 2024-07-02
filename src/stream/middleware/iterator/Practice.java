package thisisjava.stream.middleware.iterator;

import java.util.*;
public class Practice {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.addAll(Arrays.<Integer>asList(new Integer[]{10, 20, 30}));
        list.add(50);
        Iterator<Integer> iter = list.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
    }
}
