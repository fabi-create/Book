package sn.esmt.mpisi2.model;

import javax.persistence.*;

import java.io.Serializable;


@Entity
@Table(name="Reservation")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customId;
    private String name;
    private String author;

    private String nom_emprunteur;

    @ManyToOne
    @JoinColumn(name = "emprunteur_id")
    private Emprunteur emprunteur;

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
    public Emprunteur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
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


    public Reservation(int id, int customId, String name, String author,Emprunteur emprunteur) {
        this.id = id;
        this.customId=customId;
        this.name=name;
        this.author=author;
        this.emprunteur=emprunteur;
    }

    public Reservation(int id) {
        this.id = id;
    }

    public Reservation() {

    }

    public Reservation(int id, int customId, String name, String author,Emprunteur emprunteur, String nom_emprunteur) {
        this.id = id;
        this.customId=customId;
        this.name=name;
        this.author=author;
        this.emprunteur=emprunteur;
        this.nom_emprunteur=nom_emprunteur;
    }


}
