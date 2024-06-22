## 자바 21 타입추론

`var` 을 사용해서 메소드나 생성자속 지역변수에서 타입 명시를 생략할 수 있다.

대충 컴파일러가 보고서 타입을 유추하여 넣어주는 것 이기 때문에 작동가능하다.

인터페이스에서는 사용이 불가하다.

```java
import java.util.HashMap;
import java.util.Map;

public class Practice {
    // final var a = 10; 필드로 사용 불가능
    public static void main(String[] args) {
        var name = "김회창"; // 메소드 내에서 가능
        getName(name); // 메소드의 파라미터로 넣는것도 가능
        Map<String, Integer> map = new HashMap<>();
        map.put("김회창", 12); map.put("hojo choi", 15);
        for(var entry: map.entrySet()){ // 이런식으로 사용 가능
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
    public static String getName(String name){
        return name;
    }
}
```

위가 컴파일된 .class 파일에 들어가보면 아래와 같다.

```java
public class Practice {
    public Practice() {
    }

    public static void main(String[] args) {
        String name = "김회창"; // 컴파일러가 타입을 유추하여 String으로 결정
        getName(name);
        Map<String, Integer> map = new HashMap();
        map.put("김회창", 12);
        map.put("hojo choi", 15);
        Iterator var3 = map.entrySet().iterator();
        // 컴파일러가 유추한 답은 외부 반복자를 entrySet을 통해 Set 컬렉션으로부터 꺼내는것

        while(var3.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry)var3.next();
            PrintStream var10000 = System.out;
            String var10001 = (String)entry.getKey();
            var10000.println(var10001 + " : " + String.valueOf(entry.getValue()));
        }

    }

    public static String getName(String name) {
        return name;
    }
}
```
