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
