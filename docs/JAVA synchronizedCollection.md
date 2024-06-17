## 자바 동기화된 컬렉션

멀티 스레드 환경에서 Vector나 HashTable같이 애초에 동기화된 메소드를 지원하는 클래스들이 있다.

그리고 각 컬렉션 인터페이스에는 `synchronizedOOO()` 메소드를 통해, 컬렉션 인터페이스의 인스턴스를 동기화된 인스턴스로 바꿀 수 있다.

사실 Vector는 List와 비교되고, HashTable은 HashMap과 비교해서 크게 차이점이 있다면, 동기화를 지원하나 안하냐인데

왜 저렇게 정적메소드가 있는걸까

### Vector vs synchronizedList

우선에 생각해볼건 Vector의 경우 굉장히 옛날 클래스이다.

Collection이 없던 시절에 만들어졌고, 무조건적으로 모든 메소드에 synchronized 키워드가 붙어있으므로

사실상 저시절에는 동적 배열을 사용하기 위해서 단일쓰레드인 환경에서도 Vector를 사용해야만 했다.

하지만 Collection이 나온 뒤로, List 인터페이스의 구현체인 ArrayList를 사용하여 동적배열을 구현할 수 있엇고

때에 따라 멀티스레드 환경에서는 synchronizedList를 사용할 수 있다.

그리고 List 인터페이스의 다른 구현체인 LinkedList의 경우에도 동기화를 시킬 수 있다.

결과적으로는 Vector는 옛날에 동적배열을 구현하기 위해서 사용하던 클래스이고 이때는 무조건적으로 동기화된 느린 자료구조를 선택할 수 밖에 없었다.

그 이후 Collection이 나오고나서 synchronizedOOO 과 단일 스레드에서의 구현체 사용을 통해 선택지를 다양하게 가질 수 있다.

그리고 synchronized 시리즈는 별거없다. 그냥 읽기 쓰기 모두에 mutex를 활용하여 synchronized 블록을 활용했을 뿐이다.
