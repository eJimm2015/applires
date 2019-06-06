package utils;

import model.Enchere;

import java.util.Set;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class CustomTimerTask extends TimerTask {

    private Set<Enchere> encheres;
    private Enchere enchere;

    public CustomTimerTask(Set<Enchere> encheres, Enchere enchere) {
        this.encheres = encheres;
        this.enchere = enchere;
    }

    public Set<Enchere> getEncheres() {
        return encheres;
    }

    public void setEncheres(Set<Enchere> encheres) {
        this.encheres = encheres;
    }

    public Enchere getEnchere() {
        return enchere;
    }

    public void setEnchere(Enchere enchere) {
        this.enchere = enchere;
    }

    @Override
    public void run() {
        encheres = encheres.stream().map(e ->{
            if(e.getId().equals(enchere.getId())){
                if(e.getGagnant().equals(enchere.getGagnant())) e.setEtat(StatutEnchere.FINI);
            }
            return e;
        }).collect(Collectors.toSet());
    }
}
