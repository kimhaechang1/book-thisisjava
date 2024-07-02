package thisisjava.generic;

public class GenericExample {
    public static <T extends Number> boolean compare(T t1, T t2){
        // 제네릭 제한이 Number 이므로, Number 타입 메서드호출이 가능하다.
        double d1 = t1.doubleValue();
        double d2 = t2.doubleValue();
        return d1 == d2;
    }

    public static <T> boolean compareDiff(T t1, T t2){
        // 제한이 없으므로 사용할 수 있는 메서드가 Object의 메소드로 제한된다.
        return true;
    }
    public static <T> Box<T> boxing(T t){
        // 메소드 리턴타입이 제네릭타입을 사용한 경우 사용할 수 있다.
        Box<T> box = new Box<>();
        box.set(t);
        return box;
    }

    public static void main(String[] args) {
        Box<Integer> box1 = boxing(3);
        int intValue = box1.get();
        System.out.println(intValue);
        Box<String> box2 = boxing("김회창");
        String stringValue = box2.get();
        System.out.println(stringValue);


        System.out.println(compare(2,2));


    }
}
