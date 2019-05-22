package model;

import utils.StatutEnchere;

import java.io.Serializable;
import java.util.Objects;

public class Enchere implements Serializable {

    private String id, titre;
    private double prix;
    private StatutEnchere statut;

    public Enchere(String id, String titre, double prix, StatutEnchere statut) {
        this.id = id;
        this.titre = titre;
        this.prix = prix;
        this.statut = statut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public StatutEnchere getStatut() {
        return statut;
    }

    public void setStatut(StatutEnchere statut) {
        this.statut = statut;
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

    @Override
    public String toString() {
        return "Enchere{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", prix=" + prix +
                ", statut=" + statut +
                '}';
    }
}
