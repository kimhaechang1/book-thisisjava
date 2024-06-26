## 순차 컬렉션

자바에서는 `Collection` 인터페이스 내에 순서를 보장할 수 있는 다양한 인터페이스 혹은 클래스가 존재한다.

예를들자면 `Set`이지만 삽입 순서로 순서를 보장해주는 `LinkedHashSet`과 이진 검색 트리로 이루어져있다는 `TreeSet`

둘다 사실 순서가 존재한다고 표현할 수 있지만, 공통 조상 인터페이스가 존재하지 않았다.

그래서 이런 상황에 등장한것이 순차 컬렉션이다.

기존에 컬렉션 최상위 인터페이스 하위 인터페이스로 `SequencedCollection`을 추가하고

여기서 `Set`의 경우에 TreeSet의 조상 인터페이스와 LinkedHashSet의 조상 인터페이스로서 사용될 `SequencedSet`을 추가하였다.

여기서 `reverse()`메소드만을 최상위 인터페이스의 리턴 타입을 그대로 쓰는것이 아닌, `SequencedSet<E>`으로 바꾸기 위해 추상화 단계가 한겹 존재한다.

다음으로 `Map` 의 경우에도 순서를 알 수 있는 `LinkedHashMap`과 이진 검색 트리로 이루어져있다는 `TreeMap`이 있기에

추가적으로 `SequencedMap`을 만들었다.

### 주의해야 하는 점

이러한 순차 컬렉션은 '순서가 있다' 라는것을 따르는 컬렉션 인터페이스로서

마지막에 넣고/확인하고/삭제하고 처음부분에 넣고/확인하고/삭제하고 가 충분히 가능하다.

하지만, `SortedSet`과 `SortedMap`과 같은 정렬을 보장해주는 컬렉션은

사실 정렬을 보장해주기 위해 삽입 혹은 삭제 등의 연산시 내부적으로 재조정이 일어날 수 있다.

따라서 `addFirst()`와 같이 순서를 지정하여 삽입하거나 삭제하거나 등을 취하면 `Exception`이 발생한다.

### 수정할 수 없는 순차 컬렉션

지금까지 기본적인 순차 컬렉션 사용방법과 종류, 탄생 이유를 알아보았다.

추가적으로 우리가 일반적인 컬렉션에서 `List.of()`나 `Arrays.asList()`, 마지막으로 `copyOf()` 메소드등으로 불변 컬렉션을 만들 수 있었다.

추가적으로 순차 컬렉션의 경우에도 불변 컬렉션을 만들기 위해 `Collectors.unmodifiableOOO()` 메소드를 만들 었다.

`List.of()`와 비슷하다. 내부적으로 한층 더 추상화를 거쳐서 수정을 가할 수 있는 메서드들을 모두 `Exception` 터트리도록 구현해버린다.

```java
UnmodifiableSequencedCollection<E> 를 참고하면 좋다.
```
