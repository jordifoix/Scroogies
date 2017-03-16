package com.jordifoix.scroogies;

import android.text.method.DateTimeKeyListener;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jordifoix on 4/3/17.
 */

public class Deute {

    private String telefonUsuari1;
    private String nomUsuari1;
    private String telefonUsuari2;
    private String nomUsuari2;
    private Double quantitat;

    public Deute() {
    }

    public String getTelefonUsuari1() {
        return telefonUsuari1;
    }

    public void setTelefonUsuari1(String telefonUsuari1) {
        this.telefonUsuari1 = telefonUsuari1;
    }

    public String getNomUsuari1() {
        return nomUsuari1;
    }

    public void setNomUsuari1(String nomUsuari1) {
        this.nomUsuari1 = nomUsuari1;
    }

    public String getTelefonUsuari2() {
        return telefonUsuari2;
    }

    public void setTelefonUsuari2(String telefonUsuari2) {
        this.telefonUsuari2 = telefonUsuari2;
    }

    public String getNomUsuari2() {
        return nomUsuari2;
    }

    public void setNomUsuari2(String nomUsuari2) {
        this.nomUsuari2 = nomUsuari2;
    }

    public Double getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Double quantitat) {
        this.quantitat = quantitat;
    }

    public Deute(String telefonUsuari1, String nomUsuari1, String telefonUsuari2, String nomUsuari2, Double quantitat) {

        this.telefonUsuari1 = telefonUsuari1;
        this.nomUsuari1 = nomUsuari1;
        this.telefonUsuari2 = telefonUsuari2;
        this.nomUsuari2 = nomUsuari2;
        this.quantitat = quantitat;
    }
}