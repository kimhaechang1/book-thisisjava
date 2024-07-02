package thisisjava.network.chat.server;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    ChatServer chatServer;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    String clientIp;
    String chatName;

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
