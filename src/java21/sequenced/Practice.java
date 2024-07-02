package thisisjava.java21.sequenced;

import java.util.*;

public class Practice {
    public static void main(String[] args) {
        SequencedCollection<Integer> sequencedCollection = new LinkedList<>();
        sequencedCollection.addFirst(10);
        sequencedCollection.addFirst(101);
        sequencedCollection.addLast(101);
        System.out.println(sequencedCollection); // [101, 10, 101]
        SequencedCollection<Integer> list = Collections.unmodifiableSequencedCollection(sequencedCollection);
    }
}
