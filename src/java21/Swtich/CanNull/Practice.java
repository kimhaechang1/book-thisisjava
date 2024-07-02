package thisisjava.java21.Swtich.CanNull;

public class Practice {
    public static void main(String[] args) {
//        System.out.println(beforeSwitch(null)); // NullPointerException 밝생
        System.out.println(after21(null));
        System.out.println(after21("d"));

    }
    public static String beforeSwitch(String data) throws NullPointerException{
        if(data == null) throw new NullPointerException();
        switch(data){
            case "김회창" -> {
                return "레전드";
            }
            default -> {
                return "default";
            }
        }
    }
    public static String after21(String data){
        String result = switch(data){
            case "김회창" -> "레전드";
            case null, default -> "무슨일이고"; // null case 추가
            // 위와 같이 null 이거나 default로 하고싶다면 앞에 case 키워드와 함께 ,로 구분지어 작성
        };
        return result;
    }
}
