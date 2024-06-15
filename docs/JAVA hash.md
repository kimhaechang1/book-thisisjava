## hash?

우선 hash는 입력 데이터를 고정된 길이의 데이터로 매핑하는 방법을 의미함

이러한 해시를 사용하는 자료구조는 HashSet과 HashMap이 있다.

참고로 HashSet은 내부적으로 까보면 HashMap을 사용하고 있기에 HashMap을 이해하는것이 중요하다.

간단하게 HashSet의 메서드인 add()를 살펴보자면

```java
public HashSet() {
    map = new HashMap<>();
}

static final Object PRESENT = new Object();

public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}
```

이렇게 되어있다. 여기서 map은 HashMap이고 PRESENT라는 값을 value로 넣는데, 이는 그냥 더미 객체다.

그래서 HashMap을 좀 더 살펴보자.

### HashMap의 간단한 작동원리

우선 HashMap의 코드를 보면 상당히 복잡해 보이지만 간단하게 주요 포인트만 잡고 넘어가고자 한다.

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

우선 put 메서드에 key 값을 사용해서 hash()함수의 결과값을 hash값으로 사용하는것을 알 수 있다.

hash함수는 간단하게 키 값의 hashCode값을 꺼내어서, 그값을 bitShift 16 만큼 한 값과 XOR한 값을 사용한다.

즉, 간단하게 얘기하자면 해시값을 만드는 주요인이 hashCode라는점이다.

그리고 나서 마지막으로 putVal() 메서드를 보면

총 세가지의 if로 나눠져있는 주 요인만 살펴보겠다

첫 if는 말그대로 HashMap 선언이후 첫 put() 메서드일때 해당 if문 내부를 타게 된다.

사이즈가 부족해서 발생하는 것으로, 사이즈를 다시 잡아준다고 생각하면 편하다.

두번째 else if 는 구한 hash값으로 모듈러 연산을 통해 인덱스를 돌려봤더니 그 자리에 아무런 값이 없는가? 를 검사하는것이다.

대충 처음 사용되는 key일 경우 값이 저장되는거라고 생각하면 된다.

마지막 else가 좀 중요한데, 이는 해시값이 충돌났는지 검사하는 로직이다.

JAVA 8 이전에는 이중해싱을 사용하였고 그 이후에는 체이닝 기법을 사용하였다고 한다.

여기서 이제 key값 자체가 동등하고, key의 해시값 까지 같다면 완전 충돌난 상태로 간주하여, 현재 put 함수를 호출한 value값을 넣는다.

만약 둘 키가 예를들어 hash만 같거나, 값만 같거나의 경우에는 일반 충돌로 간주하여 연결리스트 끝에다가 값을 추가한다.

조금 재미난 부분은, 연결리스트의 개수가 일정 개수이상으로 초과하면, 트리화를 시도한다.

이부분은 추가로 알아봐야할듯
