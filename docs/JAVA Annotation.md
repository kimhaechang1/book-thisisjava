## 어노테이션

어노테이션은 하나의 마커라고 생각하면 편하다

컴파일러가 어노테이션을 읽어들이면서 적절한 수행을 진행한다.

보통 어노테이션은 다음의 세가지 상황에 사용된다.

1. 컴파일 시 사용하는 정보전달
2. 빌드 툴이 코드를 자동으로 생성할 때 사용하는 정보 전달
3. 실행 시 특정 기능을 처리할 때 사용하는 정보 전달

### 컴파일 시 사용하는 정보전달

대표적인 예시로는 `@Override` 어노테이션이 있다

흔히, 단축키를 사용해서 메서드를 오버라이딩 하는 경우 자동으로 IDE에서 기입하는 것으로

오버라이딩이 올바른지 컴파일시간에 검사하여, 재대로된 오버라이딩이 아니라면 컴파일 에러를 일으킨다.

### 어노테이션 타입 정의와 적용

어노테이션을 정의하는 방법은 @interface 키워드를 사용한다.

어노테이션에는 속성을 가진다.

각 속성을 정의하는 방법은 타입과 속성명으로 구분되며, 이름뒤에 괄호를 붙인다.

해당 속성값의 기본값은 default라는 키워드와 함께 넣을 수 있다.

default 키워드가 정의되어 있는 속성에는 값을 대입하지 않아도 된다.

어노테이션 속성에는 기본속성으로 value가 있다.

예를들어 다음과 같은 어노테이션이 있다.

```java
public @interface Annotation {
    String prop1() default "";
    String prop2() default "";
}
```

여기서 해당 어노테이션을 사용하려면 각 속성에 값을 대입해줘야 하고

각 속성명을 명확하게 제시해야 한다.

```java
public class AnnotationTest {
    @Annotation(prop1="김회창", prop2="")
    public static void main(String[] args) {

    }
}
```

하지만 value 속성은 기본속성이므로, 타입과 함께 어노테이션에 정의만 해준다면

다른 속성과 함께 사용하지 않을시, 어노테이션의 인자에 별다른 명시가 없더라도 자동으로 value속성에 대입된다.

```java
public @interface Annotation {
    String prop1() default "";
    String prop2() default "";
    String value();
}
```

```java
public class AnnotationTest {
    @Annotation("김회창") // 다른 두 prop1, prop2가 디폴트 값이 있다.
    public static void main(String[] args) {

    }
}
```

하지만 위의 상황에서 prop1에도 값을 넣어주기 위해서는, value를 명시해야 한다.

```java
public class AnnotationTest {
    // @Annotation("김회창", prop1="안녕") X
    // 다른 속성과 함께 대입할때에는 기본속성일지라도 속성명을 명시해야 한다.
    @Annotation(value="안녕", prop1="반갑습니다.")
    public static void main(String[] args) {

    }
}
```

### 어노테이션 적용대상

자바에서 어노테이션은 하나의 마킹을 찍어놓고 상황에 따라 적절히 실행되는거라고 했다.

그 마킹에 대상을 제한할 수 있다.

@Target 어노테이션에 속성으로 대상의 종류는 Enum타입으로 ElementType 에서 상수로 뽑아 쓰면 된다.

```java
@Target({ElementType.TYPE}) // 클래스, 인터페이스, 열거타입, 레코드 타입으로 대상을 제한한다.
public @interface Annotation {
    String prop1() default "";
    String prop2() default "";
    String value();
}
```

```java
@Annotation("안녕")
public class AnnotationTest {

    // @Annotation("안녕") 필드에서 사용할 수 없다.
    public int a;

    // @Annotation(value="안녕", prop1="반갑습니다.") //메소드에 사용할 수 없으므로
    public static void main(String[] args) {

    }
}
```

그럼 Target 어노테이션은 @Target이 ElementType.ANNOTATION_TYPE으로 되어있을 것이다.

그래야 어노테이션 선언부에서 사용할 수 있을것이다.

아래는 @Target 내용을 발췌한 것이다.

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE) // 어노테이션에서 사용할 수 있다.
public @interface Target {
    /**
     * Returns an array of the kinds of elements an annotation interface
     * can be applied to.
     * @return an array of the kinds of elements an annotation interface
     * can be applied to
     */
    ElementType[] value(); // 기본속성명을 사용하며 ElementType의 배열을 사용한 모습
}
```

### 어노테이션 유지정책

어노테이션은 언제까지 유효하게 동작할 것인지 정의할 수 있다.

`RetensionPolicy` 열거 상수를 사용해서 정의할 수 있고 종류는 다음과 같다.

1. `SOURCE`: 컴파일시 작동하고 컴파일 이후 제거된다.
2. `CLASS`: 메모리에 로딩된 후
3. `RUNTIME`: 실행할때 적용하므로, 계속 유지된다.

**SOURCE**

컴파일시에 인식하고 컴파일이 끝나면 사라져있다.

실제로 그렇게 동작하는지 알아보자.

우리가 컴파일시에 검사하는 목적으로 `@Override` 어노테이션을 사용하는 경우가 있다.

해당 어노테이션을 발췌하면 다음과 같다.

```java
@Target(ElementType.METHOD) // 메소드를 대상으로
@Retention(RetentionPolicy.SOURCE) // SOURCE이므로, 컴파일시에 한번
public @interface Override {
}
```

그럼 진짜 사라지는지 보도록 하자.

```java
class Parent{
    int method(){
        return 0;
    }
}
public class AnnotationTest extends Parent {
    @Override // 오버라이드 어노테이션
    int method() {
        return 15;
    }

    public static void main(String[] args) {

    }
}
```

컴파일 이후 AnntationTest.class 를 까보자

```java
package class1;

public class AnnotationTest extends Parent {
    public AnnotationTest() {
    }

    int method() {
        return 15;
    }

    public static void main(String[] args) {
    }
}
```

해당 어노테이션은 사라져 있는 모습을 볼 수 있다.

**CLASS**

메모리에 로딩될때 한번 실행하고 사라진다고 한다.

즉, SOURCE와는 달리 .java파일이 .class로 javac 명령어에 의해 컴파일 될때까지는 살아있다.

런타임때 해당 어노테이션이 실제로 사라졌는지 검사해보자.

아래는 사용할 어노테이션 정의와 사용되는 예시 클래스이다.

```java
@Target({ElementType.METHOD}) // 메소드로 대상을 제한한다.
@Retention(RetentionPolicy.CLASS) // CLASS이므로 클래스 로딩시까지 유지된다.
public @interface Annotation {
    String prop1() default "";
    String prop2() default "";
    String value();
}

```

```java
class Parent{

    @Annotation("안녕")
    public int method(){
        return 0;
    }
}
// public class Main ...
```

컴파일된 결과인 .class까지는 살아있다.

```java
package class1;
// Parent.class
class Parent {
    Parent() {
    }

    @Annotation("안녕")
    public int method() {
        return 0;
    }
}

```

이제 런타임환경에서 사라져있는지 검사해보자.

```java
class Parent{

    @Annotation("안녕")
    public int method(){
        return 0;
    }
}
public class AnnotationTest{

    public static void main(String[] args) {
        try{
            Class<?> clazz = Class.forName("class1.Parent"); // 리플렉션 사용
            Class<? extends java.lang.annotation.Annotation> clazzAnnotation =
                    (Class<? extends java.lang.annotation.Annotation>) Class.forName("class1.Annotation");
                    // 어노테이션에 대한 리플렉션
            Method method = clazz.getMethod("method");
            // 메소드 정보 들고옴
            java.lang.annotation.Annotation annotation = method.getAnnotation(clazzAnnotation);
            // 메소드에 적용되어 있는 어노테이션 중, @Annotation인것이 있는지 검사
            if(annotation == null){
                System.out.println("Annotation not found");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
```

```java
Annotation not found
```

이러한 CLASS 유지정책을 따르는 대표적인 어노테이션으로 롬복 라이브러리의 `@NonNull` 이 있다.

마지막으로 **RUNTIME** 이다

해당 유지정책은 런타임 환경에서도 사라지지 않는다는 것

그러면 위의 예제에서 일단 끝까지 살아있는지 검사하기 위해

`Annotation`의 `RetentionPolicy`를 `RUNTIME`으로 바꾼뒤 실행해보자.

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // 유지정책 변경
public @interface Annotation {
    String prop1() default "";
    String prop2() default "";
    String value();
}

```

```java
class Parent{

    @Annotation("안녕")
    public int method(){
        return 0;
    }
}
public class AnnotationTest{

    public static void main(String[] args) {
        try{
            Class<?> clazz = Class.forName("class1.Parent");
            Class<? extends java.lang.annotation.Annotation> clazzAnnotation =
                    (Class<? extends java.lang.annotation.Annotation>) Class.forName("class1.Annotation");
            Method method = clazz.getMethod("method");
            java.lang.annotation.Annotation annotation = method.getAnnotation(clazzAnnotation);
            if(annotation == null){
                System.out.println("Annotation not found");
            }else{
                System.out.println("Annotation found"); // 찾았을 시
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
```

```java
Annotation found
```

### 리플랙션 활용 예시: 패키지 스캔 및 인스턴스 생성

어노테이션과 리플렉션을 활용한 예시로

해당 어노테이션이 달린 클래스를 베이스 패키지에서 부터 스캔하여 찾고 인스턴스를 생성한다.

그리고 어노테이션을 처리하는 메소드를 콜하고 원본 메소드를 invoke한다.

```java
import thisisjava.baseModule.reflect.Prettier;

@Prettier(upString="&&", downString = "안녕")
public class AnnoService {

    private AnnoService(){}
    private static AnnoService instance;

    public static AnnoService getInstance(){
        if(instance == null){
            instance = new AnnoService();
        }
        return instance;
    }

    public void serviceMethod1(){
        System.out.println("call serviceMethod1");
    }

    public void serviceMethod2(){
        System.out.println("call serviceMethod2");
    }

}
```

```java
package thisisjava.baseModule.reflect;


@Prettier(upString = "*", downString = "-")
public class Service {

    private Service(){}

    private static Service service;

    public static Service getInstance(){
        if(service == null){
            service = new Service();
        }
        return service;
    }

    public String getName(Member member){
        return member.getName();
    }

    public Integer getAge(Member member){
        return member.getAge();
    }
}
```

```java
package thisisjava.baseModule.reflect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PrettierProcessor {

    static private HashMap<String, Object> instanceTable = new HashMap<>();

    public void packageScanner(String basePackage) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String path = basePackage.replace(".","/");
        Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(path);
        // 현재 실행중인 JVM의 시스템 클래스 로더가 사용하는 경로를 리턴
        // 시작옵션으로 지정된 클래스 폴더 경로가 해당됨
        List<File> classFileDirs = new ArrayList<>();
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();
            // 디렉토리 내부 폴더 및 파일들 하나씩 들고오기
            classFileDirs.add(new File(resource.getFile()));
        }

        for(File dir: classFileDirs){
            getAnnotatedClasses(dir, basePackage);
        }
    }
    private void getAnnotatedClasses(File dir, String path) throws ClassNotFoundException, UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File decodedFile = new File(URLDecoder.decode(dir.getAbsolutePath(), StandardCharsets.UTF_8));
        // 파일 인코딩 된 부분 수정
        if (!decodedFile.exists()) {
            return;
        }

        Class<? extends Prettier> annotationClass = (Class<? extends Prettier>) Class.forName("thisisjava.baseModule.reflect.Prettier");
        // 어노테이션 클래스 메타정보
        File[] files = decodedFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 찾아봤더니 폴더면 path를 이어서 붙이고 재귀호출
                getAnnotatedClasses(file, path + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                // .class로 끝나는 파일을 찾았다면
                String className = path + "." + file.getName().substring(0, file.getName().length() - 6);
                // 맨 뒤 .class를 때야한다. 왜냐하면 Class.forName()을 호출하기 위해서
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(annotationClass)) {
                    System.out.println("annotated: " + clazz.getName());
                    Method method = clazz.getMethod("getInstance");
                    if (method == null) continue;
                    instanceTable.put(clazz.getName(), method.invoke(null));
                }
            }
        }
    }

    private void doPrettier(Prettier annotation) throws ClassNotFoundException {
        System.out.println("upString: "+annotation.upString());
        System.out.println("downString: "+annotation.downString());
    }

    public void printAllInstance(){
        for(Map.Entry<String, Object> entry: instanceTable.entrySet()){
            System.out.println("key: "+entry.getKey());
        }
    }


    public void process(Class<?> clazz, String methodName, Class<?>[] types, Object ...arg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Method method;
        doPrettier(clazz.getAnnotation(Prettier.class));
        if(types == null && arg.length == 0){
            method = clazz.getMethod(methodName);
            System.out.println(instanceTable.get(clazz.getName()));
            Object result = method.invoke(instanceTable.get(clazz.getName()));
            if(!method.getReturnType().getName().equals("void")){
                System.out.println(result);
            }
        }else{
            method = clazz.getMethod(methodName, types);
            Object result = method.invoke(instanceTable.get(clazz.getName()), arg);
            if(!method.getReturnType().getName().equals("void")){
                System.out.println(result);
            }
        }

    }

    public void process(Class<?> clazz, String methodName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        process(clazz, methodName, null);
    }
}
```

```java
package thisisjava.baseModule.reflect;

import thisisjava.baseModule.reflect.anno.AnnoService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Practice {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        PrettierProcessor prettierProcessor = new PrettierProcessor();
        prettierProcessor.packageScanner("thisisjava.baseModule.reflect");
        prettierProcessor.process(AnnoService.class, "serviceMethod1");
    }
}
```

실행한 결과는 다음과 같다.

```
annotated: thisisjava.baseModule.reflect.anno.AnnoService
annotated: thisisjava.baseModule.reflect.Service
upString: &&
downString: 안녕
thisisjava.baseModule.reflect.anno.AnnoService@4b9af9a9
call serviceMethod1
```
