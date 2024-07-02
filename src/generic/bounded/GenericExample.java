package thisisjava.generic.bounded;

public class GenericExample {
    public static void main(String[] args) {
        Course.registerCourse1(new Applicant<Person>(new Person()));
        Course.registerCourse1(new Applicant<Worker>(new Worker()));
        Course.registerCourse1(new Applicant<Student>(new Student()));
        Course.registerCourse1(new Applicant<HighStudent>(new HighStudent()));
        Course.registerCourse1(new Applicant<MiddleStudent>(new MiddleStudent()));
        // 제네릭 범위가 모두이기 때문에 상관없음

        // Course.registerCourse2(new Applicant<Person>(new Person())); // 상한선 Student에 의해 막힘
        // Course.registerCourse2(new Applicant<Worker>(new Worker()));
        Course.registerCourse2(new Applicant<Student>(new Student()));
        Course.registerCourse2(new Applicant<HighStudent>(new HighStudent()));
        Course.registerCourse2(new Applicant<MiddleStudent>(new MiddleStudent()));

        Course.registerCourse3(new Applicant<Person>(new Person()));
        Course.registerCourse3(new Applicant<Worker>(new Worker()));
        // Course.registerCourse3(new Applicant<Student>(new Student())); // 하한선 Worker에 의해 막힘
        // Course.registerCourse3(new Applicant<HighStudent>(new HighStudent()));
        // Course.registerCourse3(new Applicant<MiddleStudent>(new MiddleStudent()));
    }
}
