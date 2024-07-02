package thisisjava.java21.var.Swtich.pattern;

public class Practice {
    public static void main(String[] args) {
        A a = new B();
        before(a);
        after(a);
    }

    static void before(Object obj){
        if(obj instanceof B b){
            b.method();
        }else if(obj instanceof A a){
            a.method();
        }else{
            System.out.println("버그");
        }
    }
    static void after(Object obj){
        switch(obj){
            /*
            case A a -> a.method();
            case B b -> b.method();
            Compile Error 발생
            위와 같이 작성하면 어떤 인스턴스로 만들어도 젤 위에서 걸리므로...
            아래로 내려갈수록 커야함
             */
            case B b -> b.method();
            case A a -> a.method();
            case null, default -> System.out.println("버그");
        }
    }
}
class A{
    public void method(){
        System.out.println("Aclass");
    }
}
class B extends A{
    public void method(){
        System.out.println("B Child Class");
    }
}
