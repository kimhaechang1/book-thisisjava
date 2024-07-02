package thisisjava.DataIO.chardata;

import java.io.*;
import java.util.Random;

public class Practice {
    public static void main(String[] args) throws IOException {
        File file = new File("src/thisisjava.DataIO/chardata/data.txt");
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        long startTime;
        long endTime;
        int r;
        char [] cbuf = new char[8192];
        String line;
        try (writer;bw;reader;br) {
            for(int i = 0;i<10000;i++){
                String str = makeRandomStr();
                writer.write(str);
            }
            writer.flush();

            startTime = System.nanoTime();
            while((r = reader.read()) != -1){
                reader.read();
            }
            endTime = System.nanoTime();
            System.out.println("일반 reader 사용: "+(endTime-startTime));

            startTime = System.nanoTime();
            while((r = reader.read(cbuf)) != -1){
                reader.read(cbuf);
            }
            endTime = System.nanoTime();
            System.out.println("cbuf reader 사용: cbuf길이: "+cbuf.length+" 시간: "+(endTime-startTime));

            startTime = System.nanoTime();

            while((line = br.readLine()) != null){

                br.readLine();
            }
            endTime = System.nanoTime();
            System.out.println("buffered reader 사용: "+(endTime-startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static final int LENGTH = 24;
    private static String makeRandomStr() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i= 0;i<LENGTH;i++) {
            int randomIndex = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(randomIndex));
        }
        sb.append("\n");
        return sb.toString();
    }
}
