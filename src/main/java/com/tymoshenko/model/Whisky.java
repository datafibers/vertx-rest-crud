package com.tymoshenko.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Yakiv Tymoshenko
 * @since 07.03.2016
 */
@Entity
@Table(name = "WHISKY")
public class Whisky implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORIGIN")
    private String origin;

    public Whisky() {

    }

    public Whisky(String name, String origin) {
        this.name = name;
        this.origin = origin;
    }

    public Whisky(Whisky whisky) {
        this.id = whisky.getId();
        this.name = whisky.getName();
        this.origin = whisky.getOrigin();
    }


    //=========== Equals and HashCode ==================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Whisky whisky = (Whisky) o;

        if (getId() != whisky.getId()) return false;
        if (getName() != null ? !getName().equals(whisky.getName()) : whisky.getName() != null) return false;
        return !(getOrigin() != null ? !getOrigin().equals(whisky.getOrigin()) : whisky.getOrigin() != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        return result;
    }

    //=========== Getters ==============================================================================================
    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public long getId() {
        return id;
    }

    //=========== Setters ==============================================================================================
    public void setName(String name) {
        this.name = name;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
