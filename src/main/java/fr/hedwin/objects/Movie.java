/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: Movie.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Arrays;
import java.util.Date;

public class Movie {

    public enum Format {
        @JsonProperty("DVD") DVD("DVD"), @JsonProperty("BLURAY") BLURAY("Bluray"), @JsonProperty("NUMERIQUE") NUMERIQUE("Numérique");
        private final String indice;
        Format(String indice){
            this.indice = indice;
        }
        public static Format getIndice(String indice){
            return Arrays.stream(Format.values()).filter(t -> t.indice.equals(indice)).findFirst().orElse(null);
        }
        @Override
        public String toString() {
            return indice;
        }
    }

    @JsonProperty("id_tmdb_link")
    private int id_tmdb_link;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("format")
    private Format format;

    @JsonProperty("note")
    private int note;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date date;

    public Movie(int id_tmdb_link, String nom, Format numerique, Date date) {
        this.id_tmdb_link = id_tmdb_link;
        this.nom = nom;
        this.format = numerique;
        this.date = date;
    }

    private Movie(){}

    public Movie(String nom, Format numerique, Date date) {
        this(-1, nom, numerique, date);
    }

    public int getIdTMDBLink() {
        return id_tmdb_link;
    }

    @JsonSetter("id_tmdb_link")
    public void setIdTmdbLink(int id_tmdb_link) {
        if(id_tmdb_link == 0) this.id_tmdb_link = -1;
        else this.id_tmdb_link = id_tmdb_link;
    }

    public void setIdTMDBLink(int id_tmdb_link) {
        this.id_tmdb_link = id_tmdb_link;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public int getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
