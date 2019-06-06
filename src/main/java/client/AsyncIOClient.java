package client;


import com.google.gson.Gson;
import model.Enchere;
import model.EnchereDTO;
import utils.VerbeHTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AsyncIOClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String id;


    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws Exception {
        out.println(msg);
        return in.readLine();
    }

    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
    }

    public AsyncIOClient(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

