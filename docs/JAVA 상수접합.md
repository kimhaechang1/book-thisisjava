## Constant Folding (상수접합)

### 신기한 현상

자바에서 숫자 리터럴의 연산을 컴파일된 byte 코드인 .class 를 살펴보면 연산의 결과가 할당되어 있는 모습을 볼 수 있다.

```java
// Practice.java
public class Practice {
    public static void main(String[] args) {
        byte b = 10 + 20;
        int i = 10 + 20;
        short s = 10 + 20;
        char c = 10 + 20;
        long l = 10 + 20;
        float f =  10 + 20;
        double d = 10 + 20;
    }
}
```

```java
// Practice.class
public class Practice {
    public Practice() {
    }

    public static void main(String[] args) {
        byte b = true;
        int i = true;
        short s = true;
        char c = true;
        long l = 30L;
        float f = 30.0F;
        double d = 30.0; // 바로 할당된 모습
    }
}
```

위와 같은 현상을 상수접합 이라고 부른다.

### 리터럴과 표현식

프로그래밍에서 직접 표현한 값을 의미한다.

예를들어서 숫자 리터럴은 10, 20, 12.3

문자 리터럴은 'a', 'b' 이런게 된다.

표현식은 단일 값을 생성하기 위해서 평가될 수 있는 것을 의미한다.

이러한 표현식은 함수 호출, 연산자, 변수, 리터럴과 조합되어 사용된다.

### 상수접합 개념

상수 접합은 JAVA 컴파일러가 상수 표현식을 미리 계산하는것을 의미한다.

이렇게 미리 계산할 수 있는 값들을 계산함으로서 실행시간 최적화를 달성한다고 한다.

일반적으로 위의 코드 처럼 숫자 리터럴과 연산자를 사용해서 표현식을 그대로 변수에 할당하는 경우와

상수값이 담긴 변수들을 연산자를 사용하여 더할때 상수접합이 사용된다.

자바에서 상수를 표현하는 방법은 static final 제한자를 함께 사용하는 방법이 있다.

```java
public class Practice {
    static final int a = 10;
    static final int b = 10;
    public static void main(String[] args) {
        int p = a + b;
        System.out.println(p);
    }
}
```

이를 컴파일 하면 컴파일러가 표현식을 찾아서 미리 계산해버린다.

그래서 10 + 10을 계산한 결과인 20을 변수 p에 저장한다 라는 바이트코드를 생성한다.

```java
public class Practice {
    static final int a = 10;
    static final int b = 10;

    public Practice() {
    }

    public static void main(String[] args) {
        int p = 20;
        System.out.println(p);
    }
}
```

이에 대해서 인텔리 J에서 Shift 두번 누르고 Show Byte... 치면 byte 코드를 볼 수 있다.

그기에서 main 메서드 내부만 살펴보자면

```assembly
  public static main([Ljava/lang/String;)V
   L0
    LINENUMBER 7 L0
    BIPUSH 20
    ISTORE 1
   L1
    LINENUMBER 8 L1
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    ILOAD 1
    INVOKEVIRTUAL java/io/PrintStream.println (I)V
   L2
    LINENUMBER 9 L2
    RETURN
   L3
    LOCALVARIABLE args [Ljava/lang/String; L0 L3 0
    LOCALVARIABLE p I L1 L3 1
    MAXSTACK = 2
    MAXLOCALS = 2
```

https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.istore

https://en.wikipedia.org/wiki/List_of_Java_bytecode_instructions

위의 링크를 참고하면서 해석을 해보자

main 메서드가 해당 스택 프레임 내에 프레임으로 생길 것이고, 스택프레임 내에 로컬 변수 배열이나 피연산자 스택 등이 포함되어 있다.

대충 7번째 줄에서 BIPUSH 20을 하는걸 볼 수 있다.

번역해보면 20이라는 정수값을 byte로 stack에 푸쉬한다는 의미가 된다.

여기서 말하는 stack은 피연산자를 보관하는 stack이다.

그리고 ISTORE가 있는데, 이는 뒤에 오는 인덱스가 변수의 인덱스번호가 되고

실제 java 파일내에 p변수가 인덱스 1번 변수가 되므로

p변수에 저장한다는 의미가 된다.

즉, BIPUSH때 피연산자 스택에 20을 저장하고, ISTORE때 변수 인덱스 번호 1에 해당하는 변수에 피연산자 스택 최상단에 있는 값을 꺼내어 저장한다.

그럼 결국 이미 연산이 끝난 값을 컴파일시간에 넣고있다는 것을 확인할 수 있다.
