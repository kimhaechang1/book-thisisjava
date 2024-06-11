## 이것이 자바다 - 변수

크게는 **원시타입**과 **참조타입**으로 나눠져 있음

쉽게 생각하자면 원시타입으로 명시된 몇가지 예약어를 제외하고는

모든 타입은 참조타입이 된다.

### 정수 자료형

하나의 비트당 On/Off 개념으로 껏다 켤수 있다.

근데 생각해보면 **비트개수 != 지수승**이 아니란걸 알 수 있다.

**비트의 개수로 표현할 수 있다**라는 점에 주목하면 그 이유를 알 수 있다.

32개의 비트 중 31개가 숫자를 표현하고 **최상위 비트1개는 부호비트**를 담당하기 때문이다.

그리고 음수를 포함한 범위의 경우 상한치에 항상 -1을 해주는것을 볼 수 있는데

이는 최상위 비트를 양수의 의미인 0으로 두고 나머지를 모두 1로 했을때의 값을 의미해야 하기 때문이다.

**char** 자료형의 경우 조금 독특하게도 **부호가 없는 비트 표현**을 사용한다.

따라서 부호비트 없이 16개의 비트를 사용하는데

16개의 비트로 표현가능한 가짓수는 2^16이고, **0부터 가짓수를 셈**하기 때문에 마지막에 1을 빼주어야 한다.

long 타입 변수에 int값 보다 큰 값을 할당할 때에는 L 또는 l을 맨뒤에 붙여야 한다.

<table>
<tr>
    <td>타입</td>
    <td>크기</td>
    <td>범위</td>
</tr>
<tr>
    <td>int</td>
    <td>4 바이트(32 비트)</td>
    <td>-2^31 ~ 2^31-1</td>
</tr>
<tr>
    <td>long</td>
    <td>8 바이트(64 비트)</td>
    <td>-2^63 ~ 2^63-1</td>
</tr>
<tr>
    <td>byte</td>
    <td>1 바이트 (8 비트)</td>
    <td>-2^7 ~ 2^7-1</td>
</tr>
<tr>
    <td>char</td>
    <td>2 바이트 (16 비트)</td>
    <td>0 ~ 2^16</td>
</tr>
<tr>
    <td>short</td>
    <td>2 바이트 (16 비트)</td>
    <td>-32768 ~ 32767</td>
</tr>
</table>

### 실수 자료형

실수 자료형은 부동 소수점을 표현하기 위해 사용된다.

정밀도 계산에서 사용되며,

쉽게 말해서 얼마나 더 많은 소수점 숫자들을 표현할 수 있는가에 따라 나뉜다.

부동소수점 표현은

부호비트 + 가수부 + 지수부 로 나뉘게 된다.

대충 [0/1] + [숫자 표현] + [10 ^ n] 으로 표현한다.

float의 경우 표현할 때 젤 뒤에 f또는 F를 붙여야 한다.

실수에서 오버플로우가 발생할 경우 정수 자료형과 달리
INF로 표현되며
언더플로우가 발생할 경우 0이 대입된다.

```java
public class Practice {
    public static void main(String[] args) {
        float f1 = Float.MAX_VALUE * 2;
        System.out.println(f1); // 오버플로 발생 -> Infinity

        float f2 = Float.MIN_VALUE / 2;
        System.out.println(f2); // 언더플로 발생 -> 0.0
    }
}
```

<table>
<tr>
    <td>타입</td>
    <td>크기</td>
    <td>대략적인 소수점</td>
</tr>
<tr>
    <td>float</td>
    <td>4 바이트</td>
    <td>약 6 ~ 7자리</td>
</tr>
<tr>
    <td>double</td>
    <td>8 바이트</td>
    <td>약 15 ~ 16자리</td>
</tr>
</table>

### 형변환

형변환에 있어서 주의 해야할 점으로

작은 범위 표현에서 큰 범위 표현으로는 옮기는 것은 큰 문제가 되지않는다.

여기서 말하는 범위 표현의 기준이 byte가 아님을 생각하자.

```java
public class Practice {
    public static void main(String[] args) {
        long d = 15; // long 자료형, 8바이트
        float p = d; // float 자료형, 4바이트 (문제 없음)
        System.out.println(p); // 15.0
    }
}
```

이러한 형변환에 있어서 문제가 되는 때에는

큰 범위표현에서 작은 범위 표현으로 옮길때 이다.

예를들어서 int 표현범위 max인 값이 있다고 하자

비트로 표현하자면

```
01111111 11111111 11111111 11111111
```

여기서 해당 값을 byte 자료형으로 옮기면 하위 비트나열 부터 byte에 할당한다.

```
01111111 11111111 11111111 | 11111111 < 여기까지만
```

그래서 -1이 나오게 된다.

```java
public class Practice {
    public static void main(String[] args) {
        int intMax = 128;
        byte t = (byte) intMax; // 작은 범위로 옮길 때에는 타입 캐스팅을 명시해주어야 한다.
        System.out.println(t);
    }
}
```

형변환과 이어지는 내용으로

모든 정수형 자료형의 연산결과의 최소 타입은 int이다.

이말은 즉, 연산을 할 때 연산의 결과의 타입은 최소 int라는 것

```java
public class Practice {
    public static void main(String[] args) {
        byte a = 10;
        byte b = 20;
        // byte result = a + b; // 타입 문제 발생!
        int result = a + b;

        byte res = 10 + 20; // 문제 발생하지 않음, 왜냐하면 컴파일시간에 연산을 해서 할당하기 때문
    }
}
```

만약 더 큰 범위의 자료형이 존재한다면 해당 자료형으로 반대쪽 변수를 업 캐스팅하여 연산한다.

```java
public class Practice {
    public static void main(String[] args) {
        long a = 10;
        int t = 10;
        // int res = a + t; // 자료형 문제 발생
        long res = a + t;

        double d = 12.1;
        float f = 10.5f;
        // float res2 = d + f; // 자료형 문제 발생
        double res2 = d + f;
    }
}
```

실수 자료형의 경우 조심해야 할 점이 정밀도 연산이므로

겉으로 보기엔 둘이 같은 값이지만 실제로 비교할 시, 같지 않을수도 있다.

그래서 정수 자료형 연산으로 만들고 연산후 나눠주는것이 좋다.

```java
public class Practice {
    public static void main(String[] args) {
        double d = 1.1;
        float f = 1.1f;
        System.out.println(d == f);
    }
}
```

정수 자료형을 보면 독특하게 두 자료형의 크기는 같으나 표현 범위가 다른 녀석이 있다.

short 와 char인데, short는 음수도 표현가능하고, char는 음수표현이 불가능하다.

이는 char의 경우 아스키 코드를 따라가기 때문이다.

그래서 둘 사이에 형변환이 문제가 될 수 있다.

```java
public class Practice {
    public static void main(String[] args) {
        byte b1 = 120;
        char c1 = (char) b1; // 더 큰 크기로 옮겼지만 캐스팅을 해줘야한다.
        short s1 = 26;
        char c2 = (char) s1; // 같은 표현크기지만 캐스팅을 해줘야한다.

    }
}
```
