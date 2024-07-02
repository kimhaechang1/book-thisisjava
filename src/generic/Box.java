package thisisjava.generic;

public class Box <T>{ // 클래스의 제네릭은 타입정의를 클래스명 옆에 한다.

    private T t;
    public T get(){
        return this.t;
    }

    public void set(T t){
        this.t = t;
    }

}
