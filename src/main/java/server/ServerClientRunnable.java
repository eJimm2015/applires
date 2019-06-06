package server;

import com.google.gson.Gson;
import model.Enchere;
import model.EnchereDTO;
import utils.CustomTimerTask;
import utils.StatutEnchere;
import utils.VerbeHTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class ServerClientRunnable implements Runnable {

    private Socket clientChannel;
    private Set<Enchere> encheres;
    private Set<String> participants;
    private PrintWriter out;
    private BufferedReader in;
    private Lock lock;

    public ServerClientRunnable(Socket clientChannel, Lock lock, Set<Enchere> encheres, Set<String> participants) throws Exception {
        this.clientChannel = clientChannel;
        this.encheres = encheres;
        this.participants = participants;
        out = new PrintWriter(clientChannel.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientChannel.getInputStream()));
        this.lock = lock;
    }

    @Override
    public void run() {
        EnchereDTO input;
        Gson gson = new Gson();
            try {

                do {
                    input = gson.fromJson(in.readLine(), EnchereDTO.class);
                    final EnchereDTO finalInput = input;
                    switch (input.getVerbe()){
                        case CONNECT:
                            lock.lock();
                            boolean contains = participants.add(input.getEnchere().getAuteur());
                            lock.unlock();
                            out.println(gson.toJson(contains));
                            break;
                        case GET :
                            if(input.isFilter()) out.println(gson.toJson(encheres.stream().filter(e -> e.getGagnant().equals(finalInput.getEnchere().getAuteur()) || e.getAuteur().equals(finalInput.getEnchere().getAuteur())).collect(Collectors.toSet())));
                            else out.println(gson.toJson(encheres));
                            break;
                        case POST:
                            lock.lock();
                            encheres.add(input.getEnchere());
                            lock.unlock();
                            chrono(input.getEnchere());
                            out.println(gson.toJson(encheres));
                            break;
                        case PUT:
                            lock.lock();
                            encheres = encheres.stream().map(e -> {
                                if(e.getId().equals(finalInput.getEnchere().getId())){
                                    if(e.getEtat() == StatutEnchere.EN_COURS) {
                                        e.proposer(finalInput.getEnchere());
                                    }
                                }
                                return e;
                            }).collect(Collectors.toSet());
                            lock.unlock();
                            chrono(finalInput.getEnchere());
                            out.println(gson.toJson(encheres));
                            break;
                        case DELETE:
                            lock.lock();
                            encheres.remove(input.getEnchere());
                            lock.unlock();
                            out.println(encheres);
                            break;
                        default:
                            out.println("Salut mon cher petit client !");
                            break;
                    }

                } while(input.getVerbe() != VerbeHTTP.DISCONNECT);

            } catch (Exception e) {
            } finally {
                try {
                    clientChannel.close();
                } catch (Exception e) {
                }

            }
        }

    private void chrono(Enchere e) {
        CustomTimerTask customTimerTask = new CustomTimerTask(encheres, e);
        new Timer().schedule(customTimerTask, AsyncIOServer.DELAY);
    }

}
