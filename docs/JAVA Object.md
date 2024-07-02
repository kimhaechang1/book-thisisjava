## Object 클래스

자바에서 모든 클래스는 어떤 클래스도 상속받지 않을 시, 컴파일 단계에서 자동으로 Object 클래스를 상속받게 만든다.

Object 클래스의 중요한 것은 메소드인데 주요메소드는 다음과 같다.

### booelan equals(Object obj)

`equals` 메소드는 인자로 `Object` 타입의 객체를 받는다.

이걸로 모든 클래스의 객체에 대하여 자동변환을 통해 인자로 받을 수 있다.

`equals` 메소드는 주로 두 객체가 동일한지 동등성 비교에 사용된다.

일반적으로 재정의 하지 않고 사용한다면 각 객체별 번지값을 이용해서 동등성을 비교한다.

즉, `==` 비교와 동일하다.

```java
public class Main {
    static class Test{

    }

    public static void main(String[] args) {
        Test test1 = new Test();
        Test test2 = new Test();
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test1.hashCode());
        System.out.println(test2.hashCode());
        System.out.println(test1 == test1); // true
        System.out.println(test2 == test1); // false
        System.out.println(test1.equals(test2)); // false
        System.out.println(test1.equals(test1)); // true

    }
}
```

```java
Main$Test@b4c966a
Main$Test@2f4d3709
189568618
793589513
true
false
false
true
```

그런데 우리는 `String `클래스에 대해 어느정도 알고있다.

이 클래스는 같은 문자열이 들어간 객체여도 `new` 연산자를 각각 사용하게 되면 서로다른 객체가 생성되어서 두 객체끼리 동등비교를 하게되면 서로 다르다고 나오게 된다.

```java
public static void main(String[] args) {
    Main main = new Main();
    String str = new String("반갑습니다.");
    String str2 = new String("반갑습니다.");
    String copiedStr = str2;
    System.out.println(str == str2); // false
    System.out.println(copiedStr == str2); // true
}
```

그래서 `String` 클래스는 `Object`클래스로 부터 `equals` 메소드를 재정의 하여 내부의 문자열 값을 비교하도록 구현되어 있다.

아래는 `String` 클래스내에 실제로 `equals()` 메서드를 까본 코드이다.

```java
public boolean equals(Object anObject) {
    if (this == anObject) {
		    // 동등 비교를 먼저 한다.
		    // 즉 같은 객체 번지를 가지고 있는지를 검사하는 것
        return true;
    }
    return (anObject instanceof String aString)
		    // 동일 객체가 아니라면, 우선 String인지 검사하고
            && (!COMPACT_STRINGS || this.coder == aString.coder)
            // 다음으론 기본 Encoding UTF-8인지, 그게 아니라면 같은 인코딩을 사용하는지
            && StringLatin1.equals(value, aString.value);
            // 그리고 나서 문자열 비교에 나선다.
}
```

가장 아랫줄의 `StringLatin1`클래스의 정적 메소드인 `equals`는 아래와 같이 작성되있다.

```java
public static boolean equals(byte[] value, byte[] other) {
    if (value.length == other.length) {
        for (int i = 0; i < value.length; i++) {
        // 길이가 같다면 byte 배열 값 비교하면서
            if (value[i] != other[i]) {
            // 하나 라도 다른 byte값이 있다면 false
                return false;
            }
        }
        return true;
        // byte까지 같다면 true
    }
    return false; // 애초에 길이가 다르면 false
}
```

### hashCode()

해시코드는 해당 객체의 고유 `hashCode`를 리턴하는 메소드로서 `equals` 메소드와 함께 객체 비교시에 사용되는 메소드이다.

`hashCode`값은 객체가 생성될 때, 객체의 고유 번지값을 사용해서 `hashing`한 결과이다.

해당 고유 번지값은, 객체를 그대로 출력했을 때 나오는 값과 동일하다.

이러한 `hashCode`는 자바에서 `hashing 기법`을 사용하는 다양한 자료구조에서 사용된다.

```
HashSet, HashMap 등...
```

## 객체의 동일, 동등 비교

결국 두 객체간에 같은지 비교하기 위해서는 `hashCode`와 `equals` 메소드를 둘다 오버라이딩 해야한다.

`equals`가 `true`라면 반드시 `hashCode`는 같아야한다.

왜냐하면 번지를 기준으로 만들어내기 때문에 제공되는 매개체가 같다면 hashing된 결과도 같을 것이기 때문이다.

그런데 `hashCode`가 같다고해서 반드시 `equals`의 결과가 `true`로 이어지진 않는다.

이 말이 무슨말일까?

우리는 `hashCode`를 오버라이딩 함으로서 이해할 수 있다.

만약 `hashCode`만 같으면 같은 객체라고 가정해보자.

그러면 `hashCode`를 재정의해서 어떤 숫자가 나오도록 만들고, 두 숫자가 같은 객체일 때 두 객체에 대한 `equals`값도 `true`가 되어야 할 것이다.

```java
public static void main(String[] args) {
    Object obj1 = new Object(){
        @Override
        public int hashCode() {
            return 1;
        }
    };

    Object obj2 = new Object(){
        @Override
        public int hashCode() {
            return 1;
        }
    };

    System.out.println(obj1.equals(obj2)); // false
}
```

또한 `HashSet`에서 객체를 넣을 때, 객체의 `hashCode`를 오버라이딩하고 `equals`를 오버라이딩 하지 않는다면 다음과 같은 결과를 얻게 된다.

```java
static class Data{
    int id;
    String value;
    public Data(int id, String value){
        this.id = id;
        this.value = value;
    }

    public int hashCode(){
        return this.id;
    }
}

public static void main(String[] args) {
    HashSet<Data> hashSet = new HashSet<>();
    Data data = new Data(1, "kim");
    Data data2 = new Data(1, "kim");
    System.out.println(data.hashCode()); // 1
    System.out.println(data2.hashCode()); // 1
    hashSet.add(data);
    hashSet.add(data2);
    System.out.println(hashSet.size()); // 2???
}
```

따라서 `equals`까지 오버라이딩 해주어야 내가 원하는데로 컨트롤 할 수 있다.

```java
public class Main {

    static class Data{
        int id;
        String value;
        public Data(int id, String value){
            this.id = id;
            this.value = value;
        }

        public int hashCode(){
            return this.id;
        }

        public boolean equals(Object obj){
            return obj instanceof Data data && data.hashCode() == this.hashCode();
        }

    }

    public static void main(String[] args) {
        HashSet<Data> hashSet = new HashSet<>();
        Data data = new Data(1, "kim");
        Data data2 = new Data(1, "kim");
        System.out.println(data.hashCode()); // 1
        System.out.println(data2.hashCode()); // 1
        hashSet.add(data);
        hashSet.add(data2);
        System.out.println(hashSet.size()); // 1
    }
}
```

이와 더불어서 만약 `equals`만 오버라이딩 한다면 어떻게 될까?

```java
static class Data{
    int id;
    String value;
    public Data(int id, String value){
        this.id = id;
        this.value = value;
    }

    public boolean equals(Object obj){
        return obj instanceof Data data && this.id == data.id;
    }

}

public static void main(String[] args) {
    HashSet<Data> hashSet = new HashSet<>();
    Data data = new Data(1, "kim");
    Data data2 = new Data(1, "kim");
    System.out.println(data.hashCode()); // 1
    System.out.println(data2.hashCode()); // 1
    hashSet.add(data);
    hashSet.add(data2);
    System.out.println(hashSet.size());
    // 2??? 왜냐하면 hashCode가 다르기 때문에 equals()는 문제가 없지만 key의 hash값 생성에 hashCode가 사용되므로
}
```

따라서 `equals`만 재정의 하는 경우, `Collection`을 사용할때와 같이 `hashCode`가 의미가 생기는 경우에 문제가 되고

`hashCode`만 재정의 하는 경우, 두 객체의 비교기준에 따라 `hashCode`만 비교하지 않을수도 있기 때문에 `false`가 나올 수 있다

따라서 `hashCode`와 `equals`는 모두 오버라이딩 해주어야 한다.
