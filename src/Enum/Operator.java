package thisisjava.Enum;

enum Operation {
    PLUS("+") {
        // 익명 인스턴스이므로, 추상 메소드를 구현해야 한다.
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        public double apply(double x, double y) {
            return x - y;
        }
    },
    MULTI("*") {
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        public double apply(double x, double y) {
            return x / y;
        }
    };

    // 클래스 생성자와 멤버
    private final String symbol;
    Operation(String symbol) {
        this.symbol = symbol;
    }

    // toString을 재정의하여 열거 객체의 매핑된 문자열을 반환하도록
    @Override
    public String toString() {
        return symbol;
    }

    // 열거 객체의 메소드에 사용될 추상 메소드 정의
    public abstract double apply(double x, double y);
}