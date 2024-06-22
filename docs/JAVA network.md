## 네트워크 기초

기본적으로 네트워크에서 LAN 이라고 하는것은 특정 영역에 존재하는 컴퓨터끼리 연결한 것

WAN은 LAN을 연결한 것

IP 주소는 네트워크 어뎁터라는 장치마다 발급된다.

만약 하나의 컴퓨터에 두개의 네트워크 어뎁터가 존재한다면 두개가 생기는것

DNS: 호스트 네임을 통해 ip를 얻거나, ip를 통해 호스트 네임을 얻는 서버

실제로 `ipconfig`를 통해 nslookup `<host>` 명령어를 수행하면,

현재 내 로컬 컴퓨터와 연결된 DNS 서버의 호스트 네임과 주소를 알 수 있다.

그리고 서버 컴퓨터라면 다양한 서버 프로그램이 실행되어 있을 수 있다.

그런데 pc의 네트워크 어뎁터를 통해 구분되는건 어디까지나 pc 수준이고

해당 서버 컴퓨터 내에 내가 접근하고 싶은 서버 프로그램에 서비스를 요청하기 위해서는 추가 정보가 필요하다

이 정보가 Port 이다.

운영체제는 서버 프로그램에 대해서 프로세스 단위로 생각하기에 자원을 할당하는데, 여기서 통신이 필요하다면 Port 자원도 할당한다.

따라서 서버프로그램은 Port가 필요하게 된다.

또한 서버가 클라이언트에게 알맞은 서비스를 제공하기 위해서는 서버 입장에서도 클라이언트의 주소 및 포트를 알아야 한다.

이때, 서버와 같이 특정 포트를 사용하는것이 아닌 운영체제가 자동으로 할당하는 포트를 사용한다.

### JAVA에서 IP 주소 얻기

자바에서는 java.net 패키지 속 `InetAddress` 클래스로 표현할 수 있다.

여기서 `getAllByName(String domainName)` 메소드가 존재하는데, 이는 DNS에서 도메인 네임으로 등록된 모든 IP주소를 갖고온다.

하나의 도메인 네임에 복수개의 IP주소가 할당 된 이유는 서버의 부하를 나누기 위해서이다.

### TCP

TCP 프로토콜은 서로 연결관계를 맺고, 맺은 사람들 끼리 통신하기에 안전하고 신뢰성이 높다.

하지만 연결을 맺는 과정이라는 오버헤드가 존재한다. 이는 끊을때도 마찬가지다

연결을 맺을때에는 TCP 3 way handshake

끊을 때에는 TCP 4 way handshake

추가 키워드: 흐름제어와 혼잡제어

### UDP

UDP 프로토콜은 TCP 프로토콜에 비해 연결 요청 및 수락 과정이 없고, 일방적으로 수신자에게 데이터를 보내는 것이다.

따라서 신뢰성은 TCP만큼 확보할 수 없지만, TCP의 오버헤드가 UDP에는 없기에 속도가 상대적으로 빠르다.

### 채팅 프로그램

TCP 소켓을 사용하여 채팅 참여자 인원들을 관리하고, 한사람이 전송한 채팅을 다른 모든 구성원들에게 전송하는 채팅 서버

채팅을 보내고, 서버와 연결을 맺는 채팅 클라이언트를 만들자.

```java
// ChatServer.java

package network.chat.server;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer {

    private ServerSocket serverSocket;
    public ExecutorService threadPool = Executors.newFixedThreadPool(3);
    // 최대 연결유지 가능한 3명의 인원
    private Map<String, SocketClient> clients = Collections.synchronizedMap(new HashMap<>());
    // 채팅방 내 소켓 클라이언트 관리
    // 멀티 스레드 환경에서 서로다른 스레드에서 동시적인 요청이 발생할 수 있으므로

    private static final int PORT= 50001; // 서버 포트번호

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(); // chatServer 하나를 연다.
        Scanner scanner = new Scanner(System.in);
        try{
            chatServer.start(); // chatServer 구동 -> 서버가 클라이언트의 요청수락을 대기하게 만듬
            System.out.println("[debug] ChatServer start");
            System.out.println("서버 종료를 원한다면 q를 입력");

            while(true){
                // 즉, 메인쓰레드는 "q" 입력에 대해서 무한루프로 돌게된다.
                String key = scanner.nextLine();
                // "q" 입력시 종료되도록, 그밖의 경우에서는 종료되지 않도록
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
                // 여기의 쓰레드는 수락 후, 서버가 관리하는 소켓으로 클라이언트 정보를 관리한다.
                   Socket client = serverSocket.accept();
                   // accept() 하면 어떤 클라이언트와 소통하기 위한 소켓 생성
                   System.out.println("[debug] connected "+client);
                   SocketClient socketClient = new SocketClient(this, client);
                   // 현재 ChatServer 객체와 생성된 소켓객체를 넘김
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
            serverSocket.close(); // accept하고 새로운 클라이언트에 대응하는 소켓을 닫기
            this.threadPool.shutdownNow(); // 1대1로 연결상태를 유지하고 listen 상태를 유지하는 쓰레드 풀
            // now를 통해 강제적으로 모든 작업큐 내용을 무시한체로 닫는다.
            clients.forEach((key, client)->{
                // 서버가 유지중인 클라이언트 소켓들을 하나씩 닫음
                System.out.println("[debug] ChatServer closing client socket -> "+client.socket);
                client.close();
            });
            threadPool.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addSocketClient(SocketClient socketClient){
        // 채팅 방 내 인원을 관리하는 함수
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
```

```java
// SocketClient
// 서버에서 accept된 소켓 클라이언트들을 클라이언트의 정보와 함께 관리하는 클래스
package network.chat.server;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    ChatServer chatServer;
    // 채팅 방 정보가 담겨져 있음
    Socket socket;
    // 클라이언트당 하나의 소켓 객체가 있어야 함
    DataInputStream dis;
    // 소켓으로 들어오는 스트림 -> 서버가 관리하는 클라이언트 소켓으로 들어오는것은 해당 클라이언트가 전송했다는것
    DataOutputStream dos;
    // 소켓에서 나가는 스트림 -> 서버가 관리하는 클라이언트 소켓을 통해 내보내는것은 해당 클라이언트에게 전송했다는것
    String clientIp;
    // 소켓에 담겨있는 클라이언트 IP 정보
    String chatName;
    // 내용을 기반으로 구분하는 식별자 역할

    public SocketClient(ChatServer chatServer, Socket socket){
        try{
            this.chatServer = chatServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
            // 서버가 갖고있는 클라이언트와 통신하기 위한 소켓
            // 해당 소켓은 연결정보를 갖고있다.
            this.clientIp = isa.getHostName();
            // 여기까지는 아직 채팅 룸이 만들어진 것이 아닌, 서버 소켓과의 accept이후 서버가 관리하는 클라이언트를 위한 소켓 생성까지이다.
            receive();

            // receive() 메소드에서 incoming 명령어가 파싱되면, 그때 해당 채팅방의 클라이언트가 된다.
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void receive(){
        // 하나의 연결맺음을 완료한 소켓은 서버에서 관리하는 클라이언트 소켓이 되고
        // 그와 동시에 별도의 스레드에서 계속해서 읽기 대기상태로 메세지를 읽은후 처리하는 서비스를 가진다.
        chatServer.threadPool.submit(()->{
            // 해당 채팅서버에 있는 쓰레드풀에 작업을 추가한다.
            try{
                while(true){
                    String receiveJson = dis.readUTF();
                    System.out.println("[debug] ChatServer "+Thread.currentThread().getName()+" received: "+receiveJson);
                    JSONObject jsonObject = new JSONObject(receiveJson);
                    String command = jsonObject.getString("command");
                    switch(command) {
                        case "incoming" -> {
                            this.chatName = jsonObject.getString("data");
                            // 채팅방에 들어오는 것 이기 때문에, 닉네임이 정해지고 채팅방 관리체계에 현재 소켓클라이언트가 추가되어야 한다.
                            this.chatServer.sendToAll(this, "들어오셨습니다.");
                            this.chatServer.addSocketClient(this);
                            break;
                        }
                        case "message" -> {
                            // message의 경우 내용을 함께 읽은후 자신을 제외한 채팅방 구성원에게 메세지를 보낸다.
                            String message = jsonObject.getString("data");
                            this.chatServer.sendToAll(this, message);
                            break;
                        }
                    }
                }
            }catch(IOException e){
                // 문제가 발생했다면, 해당 클라이언트의 접속을 종료시킨다.
                chatServer.sendToAll(this, "나가셨습니다.");
                chatServer.removeSocketClient(this);
            }
        });
    }
    public void send(String json){
        try{
            System.out.println("[debug] ChatServer send "+json);
            dos.writeUTF(json);
            dos.flush();
        }catch(IOException e){
        }
    }
    public void close(){
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

```java
// ChatClient.java
// 서버 주소와 참여할 포트번호를 갖고있어야 한다.
// 본인의 포트번호는 OS로 부터 자동으로 배정받는다.

package network.chat.client;

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
    // 클라이언트에서 관리하는 소켓 객체
    DataInputStream dis;
    // 소켓으로부터 들어오는 스트림
    DataOutputStream dos;
    // 소켓을 통해 서버로 나가는 스트림
    String chatName;

    static final SocketAddress serverAddr;
    // 서버 주소를 저장하는 SocketAddress 체계
    static final int SERVER_PORT = 50001;
    // 서버 포트번호
    static final String SERVER_ADDR = "localhost";
    // 서버 주소

    static{
        serverAddr = new InetSocketAddress(SERVER_ADDR, SERVER_PORT);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JSONObject jsonObject = new JSONObject();
        try(scanner){
            ChatClient chatClient = new ChatClient();
            // 자기자신의 객체를 하나 만들고
            chatClient.connect();
            // 서버와의 연결시도
            System.out.println("대화명 입력: ");
            chatClient.chatName = scanner.nextLine();
            // 대화명이 정해졌다면, 채팅방에 입장한다는 메세지를 전달해야 한다.
            jsonObject.put("command", "incoming");
            jsonObject.put("data", chatClient.chatName);
            chatClient.send(jsonObject.toString());
            jsonObject.clear();
            chatClient.receive();
            // 받는 대기 쓰레드를 활성화 시킴
            // 별도의 스레드로 두어야 메인 스레드가 블록킹 당하지 않음
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
        // 연결에 성공 후, 클라이언트로 들어오는 스트림과 나가는 스트림객체를 획득
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

```
