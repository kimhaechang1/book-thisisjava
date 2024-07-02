package thisisjava.Enum;

public enum Week { // 열거체 이름

    // 열거 상수 정의
    MONDAY{
        @Override
        String getHello() {
            return null;
        }
    },
    TUESDAY{
        @Override
        String getHello() {
            return null;
        }
    }
    ;

    abstract String getHello();

}
