package thisisjava.lambda.pr1;

@FunctionalInterface
public interface A {

    public static void a(){
        // 정적 메서드가 있더라도 추상메서드의 개수가 1개면 상관없음
    }
    default int t(){
        // 디폴트 메서드도 매한가지
        return 0;
    }
    public int calc(int x, int y);
}
