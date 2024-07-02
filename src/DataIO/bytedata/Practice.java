package thisisjava.DataIO.bytedata;

import java.io.*;

public class Practice {
    public static void main(String[] args) throws Exception{
        File file = new File("src/thisisjava.DataIO/bytedata/test.thisisjava.db");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
//        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = new FileInputStream(file);
//        DataInputStream dis = new DataInputStream(is);
        try(os;is){
            int data = 17;
//            os.write(data);
            os.write(data);
            os.flush();
            int r;
            while((r = is.read())!= -1){
                System.out.println(is.read());
            }
        }catch(EOFException e){
            e.printStackTrace();
        }
    }
}
