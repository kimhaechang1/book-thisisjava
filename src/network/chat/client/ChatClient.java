package thisisjava.network.chat.client;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ChatClient {

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    String chatName;

    static final SocketAddress serverAddr;
    static final int SERVER_PORT = 50001;
    static final String SERVER_ADDR = "localhost";

    static{
        serverAddr = new InetSocketAddress(SERVER_ADDR, SERVER_PORT);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JSONObject jsonObject = new JSONObject();
        try(scanner){
            ChatClient chatClient = new ChatClient();
            chatClient.connect();
            System.out.println("대화명 입력: ");
            chatClient.chatName = scanner.nextLine();
            // 대화명이 정해졌다면, 채팅방에 입장한다는 메세지를 전달해야 한다.
            jsonObject.put("command", "incoming");
            jsonObject.put("data", chatClient.chatName);
            chatClient.send(jsonObject.toString());
            jsonObject.clear();
            chatClient.receive();
            System.out.println("대화명 입력 완료");
            System.out.println("대화 내용을 입력하세요. 종료를 원하시면 q를 입력하여 종료합니다.");
            while(true){
                String input = scanner.nextLine();
                if("q".equals(input)){
                    chatClient.disconnect();
                    break;
                }
                jsonObject.put("command", "message");
                jsonObject.put("data", input);
                chatClient.send(jsonObject.toString());
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void connect() throws IOException{
        socket = new Socket();
        socket.connect(serverAddr);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        System.out.println("[클라이언트] 서버 연결됨");
    }

    private void receive() throws IOException {
        Thread thread = new Thread(()->{
           // 계속 돌면서 데이터가 들어오면 화면에 출력하고 처리해야해
            try{
                while(true){
                    String received = dis.readUTF();
                    JSONObject json = new JSONObject(received);
                    String clientIp = json.getString("clientIp");
                    String chatName = json.getString("chatName");
                    String message = json.getString("message");
                    System.out.println("<"+chatName+"@"+clientIp+"> "+message);
                }
            }catch(IOException e) {
                System.out.println("[클라이언트] 연결끊김");
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        if(thread.isInterrupted()){
            disconnect();
        }
    }

    private void send(String json) throws IOException{
        dos.writeUTF(json);
        dos.flush();
    }

    private void disconnect() throws IOException{
        socket.close();
    }
}
