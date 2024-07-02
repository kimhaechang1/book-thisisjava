package thisisjava.baseModule.record;

public record DTO(String name, int age, int id) implements Comparable<DTO> {

    public static class A{

    }
    public record p(String a){

    }
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
