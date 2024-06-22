## 레코드 패턴

JAVA 14 에 도입된 <a href="./JAVA Record.md">레코드</a> 는 불변 값을 필드로 가지는 객체를 선언하기 위해 사용된다.

이러한 레코드도 패턴 매칭이 가능한데, 여기서 필드값을 바로 가져오는 패턴이 가능하다.

해당 레코드 필드의 순서와 그에따른 타입만 알고있다면, 필드 값에 `Getter` 사용없이 바로 접근이 가능하다.

```java
record Student(Long id, String name){

}
record Teacher(Long id, String name){

}

public class Practice {

    static void before(Object obj){
        // ~ JAVA 17
        if(obj instanceof Student student){
            System.out.println(student.getClass().getName()+" "+student.id()+" "+student.name());
        }else if(obj instanceof Teacher teacher){
            System.out.println(teacher.getClass().getName()+" "+teacher.id()+" "+teacher.name());
        }else{
            System.out.println("누구임");
        }
        switch(obj){
            case Student student ->  System.out.println(student.getClass().getName()+" "+student.id()+" "+student.name());
            case Teacher teacher -> System.out.println(teacher.getClass().getName()+" "+teacher.id()+" "+teacher.name());
            case null, default -> System.out.println("누구임");
        }
    }

    static void after(Object obj){
        switch(obj){
            // JAVA 21
            case Student(Long id, String name) -> System.out.println(Student.class.getName()+" "+id+" "+name);
            case Teacher(Long id, String name) -> System.out.println(Teacher.class.getName()+" "+id+" "+name);
            case null, default -> System.out.println("누구임");
        }
    }

    public static void main(String[] args) {
        Student student = new Student(11L, "김회창");
//        before(student);
        after(student);
    }
}
```
