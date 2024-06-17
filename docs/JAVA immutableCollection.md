## 불변 컬렉션

각 컬렉션 인터페이스의 `of()` 정적 메소드를 통해 만들 수 있다.

또한 `copyOf()` 와 배열을 통해 수정할 수 없는 List 컬렉션을 만들 수 있다. -> `Arrays.asList()`

이게 어떻게 가능한지 한번 내부를 보자.

우선 of를 살펴보면

```java
static <E> List<E> of(E... elements) {
    switch (elements.length) { // implicit null check of elements
        case 0:
            @SuppressWarnings("unchecked")
            var list = (List<E>) ImmutableCollections.EMPTY_LIST;
            return list;
        case 1:
            return new ImmutableCollections.List12<>(elements[0]);
        case 2:
            return new ImmutableCollections.List12<>(elements[0], elements[1]);
        default:
            return ImmutableCollections.listFromArray(elements);
    }
}
```

위 처럼 ImmutableCollection의 정적 메소드 List12 혹은 listFromArray를 호출한다.

여기서 ImmutableCollections를 살펴보면

```java
static UnsupportedOperationException uoe() { return new UnsupportedOperationException(); }

@jdk.internal.ValueBased
abstract static class AbstractImmutableCollection<E> extends AbstractCollection<E> {
    // all mutating methods throw UnsupportedOperationException
    @Override public boolean add(E e) { throw uoe(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw uoe(); }
    @Override public void    clear() { throw uoe(); }
    @Override public boolean remove(Object o) { throw uoe(); }
    @Override public boolean removeAll(Collection<?> c) { throw uoe(); }
    @Override public boolean removeIf(Predicate<? super E> filter) { throw uoe(); }
    @Override public boolean retainAll(Collection<?> c) { throw uoe(); }
}
```

이런식으로 나와있는데, 독특하다고 하면 abstract 내부 클래스를 만들고,

AbstractCollection을 상속받은것을 확인할 수 있다.

AbstractCollection은 Collection인터페이스의 추상화된 메소드가 구현되어있고,

이들 중 값에 수정을 일으키는 메소드들을 AbstractImmutableCollection 내부 추상클래스를 한겹 더 둬서 전부 예외를 던지도록 재정의 되어있다.

```java
static final class ListN<E> extends AbstractImmutableList<E>
            implements Serializable
```

저렇게 새롭게 정의한 AbstractImmutableCollection의 구현체를 기준으로

각 컬렉션 인터페이스별로 추가적인 메소드가 있으니까 AbstractImmutableList로 한번더 추상화 하여서 2차적으로 수정을 일으키는 메소드들을 예외를 던진다.

그리고 AbstractImmutableOOO을 가지고 ListN을 만들고 위와같은 방식으로 SetN, MapN이 정의되어있다.

그리고 Arrays.asList() 의 경우가 좀 독특한게, Arrays 클래스에는 내부클래스로 ArrayList가 있다는 점이다.

주의 할 점으로는, 이 내부클래스는 기존에 java.util.ArrayList 와는 다른 ArrayList라는 점이다.

```java
private static class ArrayList<E> extends AbstractList<E>
        implements RandomAccess, java.io.Serializable
{
    @java.io.Serial
    private static final long serialVersionUID = -2764017481108945198L;
    @SuppressWarnings("serial") // Conditionally serializable
    private final E[] a;

    ArrayList(E[] array) {
        a = Objects.requireNonNull(array);
    }
}
```

위가 Arrays에 있는 내부클래스인데, 여기에는 final 변수로 ArrayList의 데이터들을 관리한다.

중요한점은 AbstractList에 있는 몇개의 메소드들을 재정의 하지 않은 점인데

원본 메소드를 살펴보면 불변 리스트를 유지하는데 재정의가 필요없음을 알 수 있다.

```java
public boolean add(E e) {
    add(size(), e);
    return true;
}
public void add(int index, E element) {
    throw new UnsupportedOperationException();
}
```

즉, AbstractList의 add는 단순히 넣는순간, 바로 Exception이 발생하는 구조이므로, add를 할수 없다.

java.util.ArrayList의 경우에는 동일하게 AbstractList를 상속받지만 add 메소드를 재정의 하였기에 괜찮다.

```java
public boolean add(E e) {
    // 오버라이딩 됨
    modCount++;
    add(e, elementData, size);
    return true;
}
private void add(E e, Object[] elementData, int s) {
    // 이건 오버라이딩된 메소드가 아님
    if (s == elementData.length)
        elementData = grow();
    elementData[s] = e;
    size = s + 1;
}
```

근데 Arrays로 만드는 ArrayList의 경우에는 완전한 불변은 아닌게, set 메소드가 동작한다.

이점이 of로 생성하는 List와의 차이라고 볼수 있다.

```java
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
```
