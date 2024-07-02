package thisisjava.network.hojo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.json.JSONObject;

// 클라이언트와 1:1로 통신하는 역할
// chatServer 필드는 ChatServer()의 메서드를 호출하기 위함
// socket 필드는 연결을 끊을 때 필요함
// dis, dos 필드는 문자열을 읽고 보내기 위한 보조 스트림
// clientIp와  chatName은 클라이언트의 IP 주소와 대화명
public class SocketClient {

    ChatServer chatServer;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    String clientIp;
    String chatName;

    public SocketClient(ChatServer chatServer, Socket socket) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
            this.clientIp = isa.getHostName();
            receive();
        } catch (IOException e) {

        }
    }

    // 클라이언트가 보낸 JSON 메시지를 읽는 메서드
    public void receive() {
        chatServer.threadPool.execute(() -> {
            try {
                while (true) {
                    String receiveJson = dis.readUTF();
                    JSONObject jsonObject = new JSONObject(receiveJson);
                    String command = jsonObject.getString("command");

                    switch(command) {
                        case "incoming":
                            this.chatName = jsonObject.getString("data");
                            chatServer.sendToAll(this, "들어오셨습니다.");
                            chatServer.addSocketClient(this);
                            break;
                        case "message":
                            String message = jsonObject.getString("data");
                            chatServer.sendToAll(this, message);
                            break;
                    }
                }
            } catch (IOException e) {
                chatServer.sendToAll(this, "나가셨습니다.");
                chatServer.removeSocketClient(this);
            }
        });
    }

    // JSON 메시지를 보내는 역할
    // ChatServer의 sendToAll() 메서드에서 쓰임
    public void send(String json) {
        try {
            dos.writeUTF(json);
            dos.flush();
        } catch (IOException e) {
            
        }
    }
    
    // 클라이언트와 연결을 끊는 메서드
    // ChatServer의 stop() 메서드에서 호출됨
    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            
        }
    }

}
