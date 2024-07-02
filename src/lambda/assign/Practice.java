package thisisjava.lambda.assign;

@FunctionalInterface
interface Prompt{

    String hello(String name);
}

class PromptProcessor{
    private Prompt prompt; // 함수형 인터페이스를 할당받아 저장한다.

    public void setPrompt(Prompt prompt){
        this.prompt = prompt;
    }

    public String doPrompt(String name){
        return prompt.hello(name);
    }
}

public class Practice {
    public static void main(String[] args) {
        PromptProcessor promptProcessor = new PromptProcessor();
        promptProcessor.setPrompt((name) -> name+"님 반갑습니다."); // 여기서 할당
        System.out.println(promptProcessor.doPrompt("김회창"));
        promptProcessor.setPrompt((name) -> name+" 어서오고"); // 새로운 람다식으로 할당
        System.out.println(promptProcessor.doPrompt("김회창"));
    }
}
