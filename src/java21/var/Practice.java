package thisisjava.java21.var;

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
