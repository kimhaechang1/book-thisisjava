package thisisjava.baseModule.record;

import java.util.HashSet;

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
