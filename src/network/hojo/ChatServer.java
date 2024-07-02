package thisisjava.network.hojo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

public class ChatServer {

    // serverSocket은 클라이언트의 연결 요청 수락
    ServerSocket serverSocket;
    // threadPool은 100개의 클라이언트가 동시에 채팅할 수 있도록 함
    ExecutorService threadPool = Executors.newFixedThreadPool(100);
    // chatRoom은 통신용 SocketClient를 관리하는 동기화된 Map 컬렉션
    Map<String, SocketClient> chatRoom = Collections.synchronizedMap(new HashMap<>());

    // 채팅 서버 시작할 때 호출되는 메서드
    // 50001번 포트에 바인딩하는 ServerSocket을 생성하고 작업 스레드가 처리할 Runnable을 람다식으로 제공
    // 람다식: accept() 메서드로 연결 수락하고 통신용 SocketClient를 반복해서 생성
    public void start() throws IOException {
        serverSocket = new ServerSocket(50001);
        System.out.println("[서버] 시작");

        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    SocketClient sc = new SocketClient(this, socket);
                }
            } catch (IOException e) {

            }
        });
        thread.start();
    }

    // 연결된 클라이언트의 SocketClient를 chatRoom에 추가 (key: chatName@clientIp, value: SocketClient)
    public void addSocketClient(SocketClient socketClient) {
        String key = socketClient.chatName + "@" + socketClient.clientIp;
        chatRoom.put(key, socketClient);
        System.out.println("입장: " + key);
        System.out.println("현재 채팅자 수: " + chatRoom.size() + "\n");
    }

    // 연결이 끊긴 클라이언트의 SocketClient를 chatRoom에서 제거
    public void removeSocketClient(SocketClient socketClient) {
        String key = socketClient.chatName + "@" + socketClient.clientIp;
        chatRoom.remove(key);
        System.out.println("퇴장: " + key);
        System.out.println("현재 채팅자 수: " + chatRoom.size() + "\n");
    }

    // JSON 메시지를 생성해 채팅방에 있는 모든 클라이언트에게 보내는 메서드
    // JOSN 메시지의 attribute: clientIp, chatName, message
    public void sendToAll(SocketClient sender, String message) {
        JSONObject root = new JSONObject();
        root.put("clientIp", sender.clientIp);
        root.put("chatName", sender.chatName);
        root.put("message", message);
        String json = root.toString();

        Collection<SocketClient> socketClients = chatRoom.values();
        for (SocketClient sc : socketClients) {
            if (sc == sender) continue;
            sc.send(json);
        }
    }

    // 서버 종료하는 메서드
    public void stop() {
        try {
            serverSocket.close();
            threadPool.shutdown();
            chatRoom.values().stream().forEach(sc -> sc.close());
            System.out.println("[서버] 종료");
        } catch (IOException e) {

        }
    }

    // 메인 스레드
    // 채팅 서버를 시작하기 위해 ChatServer 객체를 생성하고 start() 메서드 호출
    // q를 입력하면 stop() 메서드를 호출해서 채팅 서버 종료
    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.start();

            System.out.println("=============================================");
            System.out.println("서버를 종료하려면 q를 입력하고 Enter");
            System.out.println("=============================================");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String key = scanner.nextLine();
                if (key.equals("q")) break;
            }

            scanner.close();
            chatServer.stop();
        } catch (IOException e) {
            System.out.println("[서버] " + e.getMessage());
        }
    }

}
