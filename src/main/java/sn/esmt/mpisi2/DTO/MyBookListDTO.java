package sn.esmt.mpisi2.DTO;

import sn.esmt.mpisi2.model.Emprunteur;

import javax.persistence.*;
import java.sql.Date;

public class MyBookListDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customId;
    private String name;
    private String author;

    private String nom_emprunteur;

    private Date date_emprunt;

    private Date date_retour_prevue;


    private Date date_retour;

    private long emprunteur;

    private long penalite;

    public long getPenalite() {
        return penalite;
    }

    public void setPenalite(long penalite) {
        this.penalite = penalite;
    }


    public Date getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(Date date_retour) {
        this.date_retour = date_retour;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public Date getDate_emprunt() {
        return date_emprunt;
    }

    public void setDate_emprunt(Date date_emprunt) {
        this.date_emprunt = date_emprunt;
    }
    public String getNom_emprunteur() {
        return nom_emprunteur;
    }

    public void setNom_emprunteur(String nom_emprunteur) {
        this.nom_emprunteur = nom_emprunteur;
    }

    public Date getDate_retour_prevue() {
        return date_retour_prevue;
    }

    public void setDate_retour_prevue(Date date_retour_prevue) {
        this.date_retour_prevue = date_retour_prevue;
    }

    public MyBookListDTO() {
        super();
        // TODO Auto-generated constructor stub
    }
    public MyBookListDTO(int customId, String name, String author) {
        super();
        this.customId = customId;
        this.name = name;
        this.author = author;

    }

    public MyBookListDTO( int customId, String name, String author, String nom_emprunteur,Date date_emprunt, Date date_retour_prevue) {
        super();
        this.customId=customId;
        this.name = name;
        this.author = author;
        this.nom_emprunteur=nom_emprunteur;
        this.date_emprunt=date_emprunt;
        this.date_retour_prevue=date_retour_prevue;
    }

    public MyBookListDTO( int customId, String name, String author, String nom_emprunteur,Date date_emprunt, Date date_retour_prevue,long emprunteur) {
        super();
        this.customId=customId;
        this.name = name;
        this.author = author;
        this.nom_emprunteur=nom_emprunteur;
        this.date_emprunt=date_emprunt;
        this.date_retour_prevue=date_retour_prevue;
        this.emprunteur=emprunteur;
    }

    public MyBookListDTO( int customId, String name, String author, String nom_emprunteur,Date date_emprunt, Date date_retour_prevue, Date date_retour ) {
        super();
        this.customId=customId;
        this.name = name;
        this.author = author;
        this.nom_emprunteur=nom_emprunteur;
        this.date_emprunt=date_emprunt;
        this.date_retour_prevue=date_retour_prevue;
        this.date_retour=date_retour;
    }

    public MyBookListDTO( int customId, String name, String author, String nom_emprunteur,Date date_emprunt, Date date_retour_prevue, Date date_retour , long penalite) {
        super();
        this.customId=customId;
        this.name = name;
        this.author = author;
        this.nom_emprunteur=nom_emprunteur;
        this.date_emprunt=date_emprunt;
        this.date_retour_prevue=date_retour_prevue;
        this.date_retour=date_retour;
        this.penalite=penalite;
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

    public long getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(long emprunteur) {
        this.emprunteur = emprunteur;
    }
}
