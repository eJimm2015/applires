package model;

import utils.StatutEnchere;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Enchere implements Serializable {

    private String id, titre, auteur, gagnant = "";
    private double prix;
    private StatutEnchere etat;

    public Enchere() {
        this("", 0, StatutEnchere.EN_COURS);
    }


    public Enchere(String titre, double prix, StatutEnchere etat) {
        this.id = UUID.randomUUID().toString();
        this.titre = titre;
        this.prix = prix;
        this.etat = etat;
    }

    public Enchere setAuteur(String auteur) {
        this.auteur = auteur;
        return this;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getId() {
        return id;
    }

    public Enchere setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public Enchere setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public double getPrix() {
        return prix;
    }

    public Enchere proposer(Enchere e){
        boolean mieux = e.getPrix() > prix;
        if(mieux) {
            this.prix = e.getPrix();
            this.gagnant = e.getGagnant();
        }
        return this;
    }

    public Enchere setPrix(double prix) {
        this.prix = prix;
        return this;
    }



    public StatutEnchere getEtat() {
        return etat;
    }

    public Enchere setEtat(StatutEnchere etat) {
        this.etat = etat;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enchere enchere = (Enchere) o;
        return id.equals(enchere.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getGagnant() {
        return gagnant;
    }

    public Enchere setGagnant(String gagnant) {
        this.gagnant = gagnant;
        return this;
    }

    @Override
    public String toString() {
        return "Enchere{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", gagnant='" + gagnant + '\'' +
                ", prix=" + prix +
                ", etat=" + etat +
                '}';
    }
}
