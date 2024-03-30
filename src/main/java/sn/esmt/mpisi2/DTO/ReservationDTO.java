package sn.esmt.mpisi2.DTO;

import sn.esmt.mpisi2.model.Emprunteur;

import javax.persistence.*;

public class ReservationDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customId;
    private String name;
    private String author;

    private String nom_emprunteur;

   private long emprunteur;

    public void setEmprunteur(long emprunteur) {
        this.emprunteur = emprunteur;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }


    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getNom_emprunteur() {
        return nom_emprunteur;
    }

    public void setNom_emprunteur(String nom_emprunteur) {
        this.nom_emprunteur = nom_emprunteur;
    }


    public ReservationDTO(int id, int customId, String name, String author,long emprunteur) {
        this.id = id;
        this.customId=customId;
        this.name=name;
        this.author=author;
        this.emprunteur=emprunteur;
    }

    public ReservationDTO(int id) {
        this.id = id;
    }

    public ReservationDTO() {

    }

    public ReservationDTO(int id, int customId, String name, String author,long emprunteur, String nom_emprunteur) {
        this.id = id;
        this.customId=customId;
        this.name=name;
        this.author=author;
        this.emprunteur=emprunteur;
        this.nom_emprunteur=nom_emprunteur;
    }

    public long getEmprunteur() {
        return emprunteur;
    }
}
