package client;


import com.google.gson.Gson;
import model.User;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.Future;

public class AsyncIOClient {

    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8080);
        Future<Void> future = client.connect(hostAddress);
        future.get();
        User momo = new User().setPseudo("Momo");
        Gson gson = new Gson();
        String wrap = gson.toJson(momo);
        System.out.println("wrap : "+wrap);
        ByteBuffer buffer = ByteBuffer.wrap(wrap.getBytes(), 0, wrap.getBytes().length);
        Future<Integer> writeResult = client.write(buffer);

        // do some computation

        writeResult.get();
        buffer.flip();
        Future<Integer> readResult = client.read(buffer);

        // do some computation

        readResult.get();
        String echo = new String(buffer.array()).trim();
        buffer.clear();
        System.out.println("AsyncIOServer "  + echo);
    }
}

