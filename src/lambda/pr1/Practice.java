package thisisjava.lambda.pr1;

public class Practice {
    public static void main(String[] args) {
        // 원래라면 action 메서드의 인자로 넣을 구현 객체로, 익명 구현 객체를 만들어서 실행한다.
        action(new A(){;
            @Override
            public int calc(int x, int y) {
                return x+y;
            }
        }, 2, 3);

        // 위와같이 단 하나의 추상메서드만이 존재하는 경우 함수형 인터페이스가 될 수 있다.
        // 따라서 람다식으로 표현할 수 있다.
        action((x, y) -> x+y, 2, 3);
    }
    public static void action(A a, int x, int y){
        System.out.println(a.calc(x, y));
    }
}
