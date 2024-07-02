package thisisjava.polymorphism;

public class Overload {

    protected void originMethod(int a, int b){}
    // private void originMethod(int a, int b){} 오버로딩 규칙에 어긋난다: 파라미터 개수나 타입이 달라야함
    protected void originMethod(long a, int b){}

    // private int OriginMethod(){} 오버로딩 규칙에 어긋난다: 메소드 리턴타입이 다름
    public void originMethod(){} // 접근 제한자는 달라도 됨
}
