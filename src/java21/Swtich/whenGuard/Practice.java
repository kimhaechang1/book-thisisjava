package thisisjava.java21.var.Swtich.whenGuard;

public class Practice {
    public static void main(String[] args) {
        // 숫자를 입력하면 등급, 등급을 입력하면 숫자가 나오도록
        // swtich case를 활용하여 표현하라
        System.out.println(notUsed("A"));
        System.out.println(notUsed(90));
        System.out.println(after("A"));
        System.out.println(after(90));
    }
    static String notUsed(Object obj){
        String result = switch(obj){
            case Integer i -> {
                if(i >= 70 && i < 80){
                    yield "C";
                }else if(i >= 80 && i < 90){
                    yield "B";
                }else if(i >= 90 && i <= 100){
                    yield "A";
                }else{
                    yield "D";
                }
            }
            case String s -> {
                if("C".equals(s)){
                    yield "70 ~ 79";
                }else if("B".equals(s)){
                    yield "80 ~ 89";
                }else if("A".equals(s)){
                    yield "90 ~ 100";
                }else{
                    yield "D";
                }
            }
            case null, default -> "bug";
        };
        return result;
    }
    // 가드 사용
    static String after(Object obj){
        String result = switch(obj){
            case Integer i when i >= 70 && i < 80 -> "C";
            case Integer i when i >= 80 && i < 90 -> "B";
            case Integer i when i >= 90 && i <= 100 -> "A";
            case String s when "A".equals(s) -> "90 ~ 100";
            case String s when "B".equals(s) -> "80 ~ 89";
            case String s when "C".equals(s) -> "70 ~ 79";
            case null, default -> "bug";
        };
        return result;
    }
}
