package thisisjava.network.chat.server;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private ServerSocket serverSocket;
    public ExecutorService threadPool = Executors.newFixedThreadPool(3);
    private Map<String, SocketClient> clients = Collections.synchronizedMap(new HashMap<>());

    private static final int PORT= 50001;

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(); // chatServer 하나를 연다.
        Scanner scanner = new Scanner(System.in);
        try{
            chatServer.start(); // chatServer 구동 -> 서버가 클라이언트의 요청수락을 대기하게 만듬
            System.out.println("[debug] ChatServer start");
            System.out.println("서버 종료를 원한다면 q를 입력");

            while(true){
                String key = scanner.nextLine();
                if("q".equals(key)) break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        scanner.close();
        chatServer.stop();
        System.out.println("[debug] ChatServer stop");
    }

    private void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("[서버] 시작됨 port: "+PORT);
        Thread thread = new Thread(()->{
           // 서버를 연 다음부터 클라이언트의 요청수락은 main 쓰레드가 아닌 별도의 쓰레드에서 진행해야한다.
           // 왜냐하면 main쓰레드의 흐름이 블로킹 되면 안되기 때문
           try{
               while(true){
                   Socket client = serverSocket.accept(); // accept() 하면 어떤 클라이언트와 소통하기 위한 소켓 생성
                   System.out.println("[debug] connected "+client);
                   SocketClient socketClient = new SocketClient(this, client); // 현재 ChatServer 객체와 생성된 소켓객체를 넘김
               }
           }catch(IOException e){
           }
        });
        thread.interrupt();
        thread.start();
    }

    private void stop(){
        try{

            // 서버가 닫힐려면...
            serverSocket.close(); // accept하고 새로운 클라이언트에 대응하는 소켓
            this.threadPool.shutdownNow(); // 1대1로 연결상태를 유지하고 listen 상태를 유지하는 thisisjava.thread
            clients.forEach((key, client)->{ // 서버가 유지중인 클라이언트 소켓들을 하나씩 닫음
                System.out.println("[debug] ChatServer closing client socket -> "+client.socket);
                client.close();
            });
            threadPool.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addSocketClient(SocketClient socketClient){
        String key = socketClient.chatName+"@"+socketClient.clientIp;
        clients.put(key, socketClient);
        System.out.println("[입장] "+key);
        System.out.println("[인원] "+clients.size()+"\n");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientIp", socketClient.clientIp);
        jsonObject.put("chatName", socketClient.chatName);
        jsonObject.put("message", "[echo] 채팅방 접속 성공");
        socketClient.send(jsonObject.toString());
    }

    public void removeSocketClient(SocketClient socketClient){
        String key = socketClient.chatName+"@"+socketClient.clientIp;
        clients.remove(key);
        System.out.println("[나감] "+key);
        System.out.println("[인원] "+clients.size()+"\n");
    }

    public void sendToAll(SocketClient sender, String message){
        // sender를 제외한 모든 사용자에게 전송
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientIp", sender.clientIp);
        jsonObject.put("chatName", sender.chatName);
        jsonObject.put("message", message);
        String json = jsonObject.toString();

        Collection<SocketClient> socketClients =  clients.values();
        for(SocketClient socketClient: socketClients){
            if(socketClient == sender){
                continue;
            }
            socketClient.send(json);
        }
    }
}
