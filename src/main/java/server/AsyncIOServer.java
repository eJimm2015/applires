package server;

import com.google.gson.Gson;
import model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.Future;

public class AsyncIOServer {

    public static void main(String[] args) {
        //run server
        AsyncIOServer server = new AsyncIOServer();
        server.startAsyncServer();
    }

    private void startAsyncServer() {
        try {

            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost", 8080));
            do {
            Future<AsynchronousSocketChannel> acceptFuture = serverChannel.accept();
            AsynchronousSocketChannel clientChannel = acceptFuture.get();
            if ((clientChannel != null) && (clientChannel.isOpen())) {
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    Future<Integer> readResult  = clientChannel.read(buffer);

                    // perform other computations

                    readResult.get();
                    buffer.flip();
                String clientInput = new String(buffer.array()).trim();

                Gson gson = new Gson();
                User user = gson.fromJson(clientInput, User.class);
                System.out.println(user);
                Future<Integer> writeResult = clientChannel.write(buffer);

                    // perform other computations

                    writeResult.get();
                    buffer.clear();
                }
                //       clientChannel.close();
                //       serverChannel.close();
            } while(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
