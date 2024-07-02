package thisisjava.lambda.constructorRef;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class Data{
    int a;
    int b;
    public Data(){

    }
    public Data(int a, int b){
        this.a = a;
        this.b= b;
    }
    public Data(int a){
        this(a, 10);
    }

    public Data(long a, int b){

    }

    @Override
    public String toString() {
        return "Data{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}

class DataBuilder{

    public static Data getDefault(Supplier<Data> consumer){
        return consumer.get();
    }

    public static Data getOneParam(Function<Integer, Data> function){
        return function.apply(10);
    }

    public static Data getTwoParams(BiFunction<Integer, Integer, Data> function){
        return function.apply(2, 3);
    }

    public static Data getWrong(BiFunction<Long, Integer, Data> function){
        return function.apply(10L, 19);
    }


}


public class Practice {
    public static void main(String[] args) {
         System.out.println(DataBuilder.getDefault(()-> new Data()));
        // 단순히 생성자 호출을 통해 객체를 만들어내는 역할만 수행한다면 생성자 참조로 축약 가능
        System.out.println(DataBuilder.getDefault(Data::new));
        System.out.println(DataBuilder.getOneParam(Data::new));
        System.out.println(DataBuilder.getTwoParams(Data::new));
        // 여기서 중요한점은, 지가 알아서 파라미터에 맞게 생성자를 호출한다.
         System.out.println(DataBuilder.getWrong(Data::new)); // incompatible types: invalid constructor reference
    }
}
