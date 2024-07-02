package thisisjava.stream.middleware.paralle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Practice {
    static final int MAX = 100000000;
    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> scores = new ArrayList<>();
        for(int i=0;i<MAX;i++){
            scores.add(i);
        }

        double avg = 0;
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        int size = scores.size();
        long sum = 0;
        startTime = System.nanoTime();
        for(int number: scores){
            sum += number;
        }
        endTime = System.nanoTime();
        System.out.println("avg"+((double)sum/size)+" For-loop 처리시간: "+(endTime - startTime));

        Stream<Integer> stream = scores.stream();
        startTime = System.nanoTime();
        avg = stream.mapToInt(Integer::intValue)
                .average().getAsDouble();
        endTime = System.nanoTime();

        System.out.println("avg: "+avg+" 일반 스트림 처리시간: "+(endTime - startTime));

        Stream<Integer> paralleStream = scores.parallelStream();
        startTime = System.nanoTime();
        paralleStream.mapToInt(Integer::intValue)
                .average().getAsDouble();
        endTime = System.nanoTime();

        System.out.println("avg: "+avg+" 병렬 스트림 처리시간: "+(endTime - startTime));
    }
}
