package thisisjava.generic.bounded;

public class Course {

    public static void registerCourse1(Applicant<?> applicant){
        // 모든 범위 가능
        System.out.println(applicant.kind.getClass().getName()+" 이 course1 등록함");
    }
    public static void registerCourse2(Applicant<? extends Student> applicant){
        // 제네릭 상한선 Student, 즉 Student 클래스 이거나 Student를 상속받은 클래스는 들어올 수 있음
        System.out.println(applicant.kind.getClass().getName()+" 이 course2 등록함");
    }
    public static void registerCourse3(Applicant<? super Worker> applicant){
        // 제네릭 하한선 Worker, 즉 최소 Worker 클래스 이거나 Worker의 부모클래스들은 들어올 수 있음
        System.out.println(applicant.kind.getClass().getName()+" 이 course3 등록함");
    }

}
