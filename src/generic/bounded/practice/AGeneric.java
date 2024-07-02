package thisisjava.generic.bounded.practice;

public class AGeneric <T>{
    private T field;

    public AGeneric(T t){
        field = t;
    }

    public T get(){
        return this.field;
    }

}
