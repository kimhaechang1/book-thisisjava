package thisisjava.generic.bounded;

public class Applicant <T> { // 클래스 제네릭 타입 파라미터 선언은 클래스 명 뒤

    public T kind;

    public Applicant(T kind){
        this.kind = kind;
    }
}
