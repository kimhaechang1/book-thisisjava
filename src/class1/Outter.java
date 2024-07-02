package thisisjava.class1;


class Button{
    String buttonName;

    static interface ClickEventListener{
        String onClick(); // 버튼 클래스를 사용하는 사용자에게 구현을 맡긴다.
    }
    public void buttonClick(ClickEventListener eventListener){
        // 자동 변환 사용
        System.out.println(this.buttonName+"::"+eventListener.onClick());
    }

}

public class Outter { // 바깥 클래스



    public static void main(String[] args) {
        // 중첩 인터페이스는 static을 생략해도 된다.
        Button button = new Button();
        button.buttonName = "안녕하세요";
        class AClickEventHandler implements Button.ClickEventListener{
            @Override
            public String onClick() {
                return "On 클릭!";
            }
        }
        class BClickEventHandler implements Button.ClickEventListener{
            @Override
            public String onClick() {
                return "Off 클릭!";
            }
        }
        button.buttonClick(new AClickEventHandler());
        button.buttonClick(new BClickEventHandler());
        // 익명 객체 사용
        button.buttonClick(new Button.ClickEventListener() {
            @Override
            public String onClick() {
                return "Anonymous 클릭!";
            }
        });

        // 람다 표현식 사용
        button.buttonClick(()-> "thisisjava.lambda 클릭!");
    }
}

