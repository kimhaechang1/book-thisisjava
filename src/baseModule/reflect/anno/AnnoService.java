package thisisjava.baseModule.reflect.anno;

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
