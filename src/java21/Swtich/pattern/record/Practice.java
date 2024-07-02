package thisisjava.java21.Swtich.pattern.record;

record Student(Long id, String name){

}
record Teacher(Long id, String name){

}

public class Practice {

    static void before(Object obj){
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
            case Student(Long dddd, String name) -> System.out.println(Student.class.getName()+" "+dddd+" "+name);
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
