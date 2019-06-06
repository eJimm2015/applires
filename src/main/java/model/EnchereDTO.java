package model;

import utils.VerbeHTTP;

import java.util.Objects;

public class EnchereDTO {

    private VerbeHTTP verbe;
    private Enchere enchere;
    private boolean filter = false;

    public boolean isFilter() {
        return filter;
    }

    public EnchereDTO setFilter(boolean filter) {
        this.filter = filter;
        return this;
    }

    public VerbeHTTP getVerbe() {
        return verbe;
    }

    public EnchereDTO setVerbe(VerbeHTTP verbe) {
        this.verbe = verbe;
        return this;
    }

    public Enchere getEnchere() {
        return enchere;
    }

    public  EnchereDTO setEnchere(Enchere enchere) {
        this.enchere = enchere;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnchereDTO that = (EnchereDTO) o;
        return enchere.equals(that.enchere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enchere);
    }

    @Override
    public String toString() {
        return "EnchereDTO{" +
                "verbe=" + verbe +
                ", enchere=" + enchere +
                ", filter=" + filter +
                '}';
    }
}
