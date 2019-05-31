package model;

import utils.StatutEnchere;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Enchere implements Serializable {

    private String id, titre, proprietaire;
    private double prix;
    private StatutEnchere statut;

    public Enchere() {
        this("", 0, StatutEnchere.EN_COURS);
    }

    public Enchere(String titre, double prix, StatutEnchere statut) {
        this.id = UUID.randomUUID().toString();
        this.titre = titre;
        this.prix = prix;
        this.statut = statut;
    }

    public Enchere setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
        return this;
    }

    public String getProprietaire() {
        return proprietaire;
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

    public Enchere setPrix(double prix) {
        this.prix = prix;
        return this;
    }

    public StatutEnchere getStatut() {
        return statut;
    }

    public Enchere setStatut(StatutEnchere statut) {
        this.statut = statut;
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
