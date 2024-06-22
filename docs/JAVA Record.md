## 레코드

데이터 전달을 위한 DTO를 작성할 때, 반복적으로 사용되는 코드를 줄이기 우해서 JAVA 14부터 도입됨

예를들어 클라이언트로 부터 어떤 전송 객체를 받아서 처리한다고 가정한다면..

하나의 클래스를 완벽하게 정의하기 위해서 `String toString(){}`, `boolean equals(Object o){}`, `int hashCode(){}`를 재정의 해주어야 한다.

하지만 클라이언트의 변경점이 생겨서 전송객체의 변경점이 생긴다고 하면

필드만의 변화가 아니라 저기 메소드에 있는 내용의 변화도 생길수 있다.

### 레코드 사용법 및 특징

레코드는 클래스와 비슷하고 `record`라는 키워드를 사용해서 선언한다.

다른점은 사용할 필드를 파라미터로 넘긴다는 것

그렇게 파라미터로 넘긴 필드는 자동으로 `static final` 제한자가 붙게 된다.

그리고 생성자와 `Getter`가 붙게되고, 필요한 재정의한 코드들도 추가된다.

다른 인터페이스의 구현체는 될 수 있지만, 상속은 받을 수 없다.

```java
package baseModule.record;

public record DTO(String name, int age, int id) implements Comparable<DTO> {

    public void method(){
        System.out.println("name: "+name+" age: "+age+" id: "+id);
    }
    static void staticMethod(){
        System.out.println("정적 메소드 호출");
    }

    @Override
    // implements 가능
    public int compareTo(DTO o) {
        return 0;
    }
    // abstract void method1(); 레코드에는 추상 클래스가 올 수 없음

}

public class Practice {
    public static void main(String[] args) {
        DTO dto = new DTO("김회창", 27, 1);
        System.out.println(dto.age());
        System.out.println(dto.id());
        System.out.println(dto.name()); // Getter 호출
        DTO dto2 = dto;
        System.out.println(dto2.equals(dto2)); // equals 재정의 되어있음
        System.out.println(dto); // toString 재정의 되어있음
        HashSet<DTO> set = new HashSet<>();
        DTO dto3 = new DTO("김회창", 27, 1);
        System.out.println(dto.equals(dto3));
        // equals() 메소드의 경우 모든 필드를 기준으로 값이 동일한지 검사함
        set.add(dto); set.add(dto3);
        // hashCode() 메소드의 경우 필드의 hashCode 를 조합하여 만들기 때문에, 이역시 필드들의 값이 동일하면 동일해짐
        System.out.println(set.size());
        dto.method();
        DTO.staticMethod(); // 정적 메소드 호출

    }
}
```

```
27
1
김회창
true
DTO[name=김회창, age=27, id=1]
true
1
name: 김회창 age: 27 id: 1
정적 메소드 호출
```
