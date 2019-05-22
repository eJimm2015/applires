package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class User  implements Serializable {

    private String id, pseudo;


    public User() {
        this("");
    }

    public User(String pseudo) {
        this.id = UUID.randomUUID().toString();
        this.pseudo = pseudo;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getPseudo() {
        return pseudo;
    }

    public User setPseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }
}
