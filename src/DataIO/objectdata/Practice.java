package thisisjava.DataIO.objectdata;

import java.io.*;

public class Practice {

    public static void main(String[] args) throws IOException {
        int [] arr = new int[]{10, 20, 30};
        Integer [] arr2 = new Integer[]{10, 20, 30};
        arrayInfo(arr2);
    }
    static void arrayInfo(Object arr){
        Class<?> clazz = arr.getClass();
        System.out.println("className: "+clazz.getName());
        System.out.println("isArray: "+clazz.isArray());
        Class<?> [] interfaces = clazz.getInterfaces();
        for(Class<?> inter: interfaces){
            System.out.println("implements: "+inter.getName());
        }
    }

}
