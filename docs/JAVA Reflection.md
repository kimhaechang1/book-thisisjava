## Java 리플렉션

자바에서는 Class 클래스를 통해 클래스들의 정보를 얻어올 수 있다.

Class 클래스는 자바 클래스와 인터페이스의 메타정보를 담고 있다.

이러한 메타정보를 들고오는 방법으로는

```java
Class clazz = 클래스.class;
Class clazz = Class.forName("패키지 클래스이름");
Class clazz = 객체 참조변수.getClass();
```

이 있다.

여기서 멤버정보를 얻어오려면

`Field` 객체 배열, `Contstructor` 객체배열, `Method` 객체 배열이 있다.

각각 이름에서 알 수 있듯, 변수명 혹은 메서드명 타입 그리고 메서드의 경우 파라미터 정보까지 알 수 있다.

제한자의 경우에는 `Modifier`의 `toString()` 메서드에 인자로 `getModifiers()` 반환값을 넣어주면 알 수 있다.

추가적으로 어떤 클래스를 상속받았는지, 어떤 인터페이스를 `implements` 하고 있는지도 확인이 가능하다.

```java
import java.lang.reflect.*;

class Parent{

}

interface A{

}
class Frame extends Parent implements A{
    final int a = 10;
    private static int b;
    private String method(int t, String p){
        System.out.println("call method");
        return "입력값:: t: "+t+" p: "+p;
    }
    private class F{

    }
}
public class ReflectionTest {
    public static void main(String[] args) {
        try{
            Class<?> clazz = Class.forName("thisisjava.baseModule.reflect.Frame");
            System.out.println("패키지 정보: "+clazz.getPackageName());
            System.out.println("풀 패키지 네임: "+clazz.getName());
            System.out.println("심플 네임: "+clazz.getSimpleName());
            System.out.println("패키지 명: "+clazz.getPackageName());
            System.out.println("접근제한자: "+Modifier.toString(clazz.getModifiers()));
            // 접근제한자의 경우 bit로 연산된 결과이므로 Modifier 클래스의 static 메소드를 호출하면 문자열로 확인 가능하다.
            System.out.println("필드 추출: ");

            Class<?> superClazz = clazz.getSuperclass();
            // 상속하는 클래스 정보
            System.out.println("슈퍼 클래스 패키지 정보: "+clazz.getPackageName());
            System.out.println("슈퍼 클래스 풀 패키지 네임: "+clazz.getName());
            System.out.println("슈퍼 클래스 심플 네임: "+clazz.getSimpleName());
            System.out.println("슈퍼 클래스 패키지 명: "+clazz.getPackageName());
            System.out.println("슈퍼 클래스 접근제한자: "+Modifier.toString(clazz.getModifiers()));

            Class<?>[] interfaces = clazz.getInterfaces();
            for(Class<?> interFace: interfaces){
                System.out.println("인터페이스명: "+interFace.getName());
            }

            Field[] fields = clazz.getDeclaredFields();
            // 모든 제한자 상관없이 필드를 가져오되, 상속받은 필드는 가져올 수 없다.

            for(Field field: fields){
                System.out.println("필드 변수이름: "+field.getName());
                System.out.println("필드 변수타입: "+field.getType());
                int modifiers = field.getModifiers();
                System.out.println("제한자: "+modifiers);
            }
            System.out.println("메서드 추출: ");
            Method [] methods = clazz.getMethods();
            // getMethods는 상속받은 메서드 및 public메소드만 들고온다.
            clazz.getDeclaredMethods();
            // getDeclaredMethods()는 모든 접근제한자에 대해 들고오지만, 상속받은 메서드는 들고올 수 없다.

            System.out.println("생성자 추출: ");
            Constructor[] constructors = clazz.getConstructors();
            // 여기서 재밌는점
            // 기본 생성자는 클래스의 접근제한자를 따라간다.
            // 따라서 위의 Frame클래스에 명시적인 기본 생성자가 없을 경우 컴파일러가 클래스 접근제한자와 동일한 제한자를 가지는 기본 생성자를 주입한다.
            // 그럼 여기서 getConstructors()를 하면 public 생성자만 나오게 된다.
            // 그것이 아니라 모든 접근제한자의 constructor를 뽑고싶다면 getDeclaredConstructors()를 호출하면 된다.

            System.out.println("생성자 개수: "+constructors.length);
            Object obj = null;
            for(Constructor constructor: constructors){
                System.out.println("생성자 이름: "+constructor.getName());
                System.out.println("생성자 파라미터 추출: ");
                obj = constructor.newInstance();
                // 생성자를 통해 newInstance()를 호출함으로서 객체를 얻어낼 수 있다.
                Parameter [] parameters = constructor.getParameters();
                for(Parameter parameter: parameters){
                    System.out.println("생성자 파라미터 이름: "+parameter.getName());
                    System.out.println("생성자 파라미터 타입: "+parameter.getType());
                }
            }
            for(Method method: methods){
                System.out.println("메서드 타입: "+method.getReturnType());
                System.out.println("메서드 이름: "+method.getName());
                Parameter [] parameters = method.getParameters();
                if(parameters.length == 0) System.out.println("파라미터가 0개인 메서드입니다.");
                for(Parameter parameter: parameters){
                    System.out.println("파라미터 변수 이름: "+parameter.getName());
                    System.out.println("파라미터 변수 타입: "+parameter.getType());
                }

                if(method.getName().equals("method")){
                    String answer = (String) method.invoke(obj, 1, "김회창");
                    // 해당 객체를 기준으로 method를 호출할 수있으며, 첫번째 인자는 어떤 객체인지, 두번째 인자부터 가변 인자로서 인자값을 순서대로 넘긴다.
                    // 그리고 호출한 결과값을 리턴받는다.
                    System.out.println("출력결과: "+answer);
                }
            }

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
```
