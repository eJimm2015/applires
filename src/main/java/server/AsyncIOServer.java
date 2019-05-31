package server;

import com.google.gson.Gson;
import model.Enchere;
import utils.StatutEnchere;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AsyncIOServer {

    Set<Enchere> encheres = new HashSet<>();
    Set<String> participants = new HashSet<>();

    public static void main(String[] args) {

        AsyncIOServer server = new AsyncIOServer();
        server.startAsyncServer();
    }

    public boolean inscription(String pseudo){
        return  participants.add(pseudo);
    }

    public boolean proposition(String participant, String idEnchere, double prix){
        Optional<Enchere> enchereOpt = encheres.stream().filter(e -> e.getId().equals(idEnchere)).findFirst();
        if(enchereOpt.isPresent()) {
            Enchere e = enchereOpt.get();
            if(e.getPrix() < prix){
                e.setPrix(prix);
                e.setProprietaire(participant);
                return true;
            }
        }
        return false;
    }

    public Set<Enchere> mesEncheres(String participant){
        return encheres.stream().filter(e -> e.getProprietaire().equals(participant)).collect(Collectors.toSet());
    }

    public Set<Enchere> getEncheres(String participant){
        return encheres.stream().filter(e -> !e.getProprietaire().equals(participant)).collect(Collectors.toSet());
    }

    public void setStatutEncheres(String idEnchere, StatutEnchere statutEnchere){
        Optional<Enchere> enchereOpt = encheres.stream().filter(e -> e.getId().equals(idEnchere)).findFirst();
        if(enchereOpt.isPresent()){
            Enchere e = enchereOpt.get();
            e.setStatut(statutEnchere);
        }
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
                Enchere e = gson.fromJson(clientInput, Enchere.class);
                System.out.println(e);
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
