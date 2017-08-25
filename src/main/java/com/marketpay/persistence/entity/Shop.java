package com.marketpay.persistence.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private long idBu;
    private String name;
    private String location;
    private String codeAl;
    private String gln;
    private String atica;

    public Long getId() {
        return id;
    }

    public long getIdBu() {
        return idBu;
    }

    public void setIdBu(long idBu) {
        this.idBu = idBu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCodeAl() {
        return codeAl;
    }

    public void setCodeAl(String codeAl) {
        this.codeAl = codeAl;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public String getAtica() {
        return atica;
    }

    public void setAtica(String atica) {
        this.atica = atica;
    }

    @Override
    public String toString() {
        return "Shop{" +
            "id=" + id +
            ", idBu=" + idBu +
            ", name='" + name + '\'' +
            ", location='" + location + '\'' +
            ", codeAl='" + codeAl + '\'' +
            ", gln='" + gln + '\'' +
            ", atica='" + atica + '\'' +
            '}';
    }
}
