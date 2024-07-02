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
