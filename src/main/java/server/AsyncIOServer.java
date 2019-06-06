package server;
import model.Enchere;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncIOServer {

    public static final int DELAY = 20000;
    private Set<Enchere> encheres = new HashSet<>();
    private Set<String> participants = new HashSet<>();
    private ServerSocket serverSocket;
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {

        AsyncIOServer server = new AsyncIOServer();
        server.start(8080);
    }

    public AsyncIOServer() {}

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        while(true){
            Socket clientSocket = serverSocket.accept();
            new Thread(new ServerClientRunnable(clientSocket, lock, encheres, participants)).start();
        }
    }

    public void stop() throws Exception {
        serverSocket.close();
    }
}
