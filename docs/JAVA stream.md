### 내부 반복자와 외부 반복자

JAVA에서는 외부 반복자로 대표적으로 `Iterator`가 있었다.

`Iterator`는 컬렉션 인터페이스에 대해서 별도의 클래스인 `Iterator`를 활용해서 순회할 수 있도록 도와준다.

하지만 외부반복자는 멀티 스레드로 처리하기 힘들고,

반복자를 사용하는 사용자가 직접 요소를 다음으로 넘길지말지 결정짓고, 다음이 있는지 없는지 체크하여야 한다.

```java
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
```

하지만 내부 반복자의 경우, 해당하는 데이터 스트림 내부에서 순회하기 때문에

`Iterator`처럼 하나하나 사용자가 넘기는 것이 아니라,

데이터를 소유하고있는 컬렉션에서 사용자가 제공하는 함수형 인터페이스 람다식을 갖고서 동작한다.

### 스트림의 구성요소

`Stream` 은 배열과 컬렉션 모두로부터 만들 수 있다.

그리고 배열과 컬렉션으로 부터 만든 1차적인 스트림은 오리지널 스트림이라고 부르고

여기서 중간처리와 최종처리를 거쳐야 한다.

만약 중간처리만 있다면, 절대로 동작하지 않는다.

```java
public static void main(String[] args) {
    int [] arr = new int[] {10,20,30};
    List<Integer> list = Arrays.asList(10, 20, 30);
    Stream<Integer> listStream = list.stream();
    IntStream arrStream = Arrays.stream(arr);
    listStream.forEach(e -> System.out.println("listStream: "+e));
    // 바로 최종처리 메소드인 forEach 호출하여서 내부 반복자 동작
    arrStream.forEach(e -> System.out.println("arrStream: "+e));
}
```

위와 같이 자료구조를 활용하여 스트림을 얻는 방법이 있고

별도의 자료구조 없이 숫자 범위를 갖고서 스트림을 얻는 `range()`와 `rangeClosed()`가 있다.

둘의 차이로 후자로 오는 메소드는 끝 범위를 포함하는 스트림을 만든다.

```java
IntStream range = IntStream.range(1, 10);
IntStream rangeClosed = IntStream.rangeClosed(1, 10);
System.out.println("rangeStream: "+range.sum()); // 45
System.out.println("rangeClosedStream: "+rangeClosed.sum()); // 55
```

### 중간처리 메소드

중간처리 메소드에는 필터링, 루핑(peek), 매핑, 정렬이 있다.

먼저 필터링은 `filter()`, `distinct()` 메소드를 통해 요소들 중 조건에 만족하는 요소들로 새롭게 스트림을 만든다.

그 중 `distinct()`의 경우, `equals()` 메소드의 값이 true인 경우에 같은 요소로 판단한다.

```java
class Data{

    int value;

    public Data(int val){
        value = val;
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
```

Data 클래스에서 사람 입장에서는 value가 동일하다면 동일한 객체로 판단하고 있지만

컴퓨터 입장에서는 이 둘이 같은 객체인지 알 방법이 없다.

### JAVA의 동일성(Identity)과 동등성(Equality)

https://dzone.com/articles/identity-vs-equality-in-java

먼저 컴퓨터 과학에서 얘기하는 동일성은 객체를 고유하게 식별하는 무언가이다.

JAVA에서는 객체를 식별하기 위해 참조값을 사용한다.

```java
public class Practice {
    static class Data{
        // Data라는 클래스가 있다.
    }
    public static void main(String[] args) {
        Data obj1 = new Data(); // obj1이라는 Data 타입 인스턴스가 있다.
        System.out.println(obj1);
        Data obj2 = obj1;
        System.out.println(obj2);
    }
}
```

```
stream.Identity.Practice$Data@5f184fc6
stream.Identity.Practice$Data@5f184fc6

동일한 참조값을 가지고 있다.
```

Identity는 참조 동일성이라고 부르기도 한다.

즉, 참조값이 동일하다면 동일 객체로 식별한다.

```java
System.out.println(obj1 == obj2) // true
```

동등성은 두 객체가 같다는 것을 의미한다.

두 객체가 동등하다는 것이 반드시 그것들이 같은 객체라는것을 의미하진 않는다.

자바에서는 `equals()` 메소드를 통해서 두 객체가 동등한지 검사한다.

이것을 다른말로 구조적 동등이라고 부른다.

즉, JAVA에서 만약에 서로다른 new 연산자를 통한 객체가 동등하기 위해서는

서로 같은 객체는 아니더라도 (obj1 != obj2) `equals()` 메소드의 결과가 동일하다면 동등한 객체가 된다.

```java
public class Practice {
    static class Data{
        // Data라는 클래스가 있다.
        public boolean equals(Object o){
            if(o == this) return true;
            if(o != null && getClass() != o.getClass()) return false;
            Data other = (Data) o;
            return true;
        }
    }
    public static void main(String[] args) {
        Data obj1 = new Data(); // obj1이라는 Data 타입 인스턴스가 있다.
        Data obj2 = new Data();
        System.out.println(obj1 == obj2); // 동일성 비교는 false
        System.out.println(obj1.equals(obj2)); // equals() 메소드를 재정의 했기 때문에 동등성 비교 true
    }
}
```

하지만 정말로 잘 작동하는지 테스트 하기 위해서 Set 자료구조를 사용해보았다.

```java
public class Practice {
    static class Data{
        // Data라는 클래스가 있다.
        public boolean equals(Object o){
            if(o == this) return true;
            if(o != null && getClass() != o.getClass()) return false;
            Data other = (Data) o;
            return true;
        }
    }
    public static void main(String[] args) {
        Data obj1 = new Data(); // obj1이라는 Data 타입 인스턴스가 있다.
        Data obj2 = new Data();
        HashSet<Data> set = new HashSet<>();
        set.add(obj1);
        set.add(obj2);
        System.out.println(set.size() == 1); // 1개가 나오길 기대하는데 ...
    }
}
```

```
false
```

왜 `false`일까? 이는 hash를 사용하는 자료구조는 입력 데이터의 `hashCode()`의 반환값을 통해 해싱을 진행하기 때문이다.

따라서 hash를 사용하는 컬렉션에서도 동등성이 작용하기 위해서는 `hashCode()` 또한 오버라이딩 해주어야 한다.

```java
public class Practice {
    static class Data{
        // Data라는 클래스가 있다.
        public boolean equals(Object o){
            if(o == this) return true;
            if(o != null && getClass() != o.getClass()) return false;
            Data other = (Data) o;
            return true;
        }

        @Override
        public int hashCode() {
            return 1; // hashCode() 오버라이딩
        }
    }
    public static void main(String[] args) {
        Data obj1 = new Data(); // obj1이라는 Data 타입 인스턴스가 있다.
        Data obj2 = new Data();
        HashSet<Data> set = new HashSet<>();
        set.add(obj1);
        set.add(obj2);
        System.out.println(set.size() == 1); // 1개가 나오길 기대하는데 ...
    }
}
```

```java
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
```

이렇게 필드 중 value 값이 동일하면 동일하다고 판단하게 `equals()`를 재정의 해줌으로서

중복을 의도대로 제거할 수 있다.

매핑의 경우 스트림내의 데이터를 다른 종류의 데이터 스트림으로 바꾸는 목적이다.

그중에서 `flatMap()`의 경우, 오리지널 스트림의 요소들 하나하나가 스트림으로 반환될 여지가 있을 때 사용한다.

```java
public class Practice {
    public static void main(String[] args) {
        String [] inputs = new String[]{"10 20 30", "40 50"};
        Arrays.<String>stream(inputs)
                .flatMapToInt((input)->{
                    String [] scoreInput = input.split(" ");
                    return Arrays.stream(scoreInput).mapToInt(Integer::parseInt);
                }).forEach(System.out::println);
    }
}
```

루핑은 요소 내부를 순회하는 것으로

`forEach()` 가 있지만, `peek()`도 있다.

둘의 차이로는 `peek()`은 중간처리 메소드이고, `forEach()`는 최종처리 메소드이다.

사용법은 `forEach()`와 동일하다.

마지막으로 정렬이 있다.

정렬은 `sorted()` 메소드로서 `Comparable` 인터페이스의 구현 객체 혹은
`Comparator` 구현 객체가 있으면 된다.

```java
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
```

### 최종처리 메소드

최종처리 메소드에는 집계, 수집, 루핑(forEach), 매칭이 있다.

먼저 매칭은 스트림 내 요소들이 특정 조건을 만족하는지 검사한 결과를 `true` or `false`로 나타낸다.

`allMatch()`는 모든 요소가 조건에 `true`여야 하고

`anyMatch()`는 하나라도 조건에 맞으면 `true`이고

`noneMatch()`는 어느 하나라도 조건에 맞지않아야 `true`를 반환한다.

집계의 경우 요소들을 하나의 집단으로 보고

해당 집단내에 값들을 통해 어떤 하나의 값으로 산출해내는 것을 의미한다.

최대값, 최솟값, 평균, 합 등의 결과를 얻을 수 있다.

여기서 중요한 것은 `Optional` 타입을 반환하는 함수들이 있다.

`Optional`은 만약에 해당 집계 메서드의 결과가 존재하지 않는 경우에 대해서 예외 사항을 던지지않고 매끄럽게 처리하는 목적으로 사용한다.

또한 집계의 경우 다양한 집계를 커스텀하도록 `reduce()` 메소드가 지원된다.

```java
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
//        System.out.println(intStream.average().getAsDouble()); // Exception in thread "main" java.util.NoSuchElementException: No value present
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
```

수집의 경우 `Collector` 클래스의 정적 메소드를 활용해서 다른 컬렉션으로 수집한다.

JAVA 16에서 `List`의 경우 `collect()` 메소드를 사용하지 않고서 `Collector`의 정적 메소드를 바로 호출함으로서 코드가 더욱 간결해진다.

```java
public class Practice {
    static class Data{
        String name;
        int value;
        public Data(String n, int v){
            name = n;
            value = v;
        }
    }
    public static void main(String[] args) {
        List<Data> list = new ArrayList<>();
        list.add(new Data("김회창", 10));
        list.add(new Data("김김김", 20));
//        list.add(new Data("김회창", 30));
        // name이 key이고 value가 value가 되는 Map으로 변환하고 싶다
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(
                (obj)-> obj.name, // 그런데 name이 Comparable이 구현되어있는 클래스인 String으로서 동일한 key가 발생한다.
                // Exception in thread "main" java.lang.IllegalStateException: Duplicate key 김회창 (attempted merging values 10 and 30)
                // 따라서 저기 3번째 데이터를 주석하고 실행하면 정상실행 된다.
                (obj)-> obj.value
        ));
        for(Map.Entry<String, Integer> entry: map.entrySet()){
            System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
        }
    }
}
```

또한 Map 인터페이스의 경우 `Collectors`의 정적메소드 `groupingby()`를 통해 요소를 특정 기준으로 수집한 결과 리스트를 얻을 수 있다.

```java
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
                // Exception in thread "main" java.lang.IllegalStateException: Duplicate key 김회창 (attempted merging values 10 and 30)
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
```

### 스트림 병렬처리

// ForkJoinPool에 대해서 좀 더 알아볼 것
