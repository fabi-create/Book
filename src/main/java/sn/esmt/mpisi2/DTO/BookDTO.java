package sn.esmt.mpisi2.DTO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BookDTO {



    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;

    private int nombre_exemplaire;


    private String imageContent;

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

    public int getNombre_exemplaire() {
        return nombre_exemplaire;
    }

    public void setNombre_exemplaire(int nombre_exemplaire) {
        this.nombre_exemplaire = nombre_exemplaire;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public BookDTO(int id) {
        this.id = id;
    }
    public BookDTO(String name, String author, int nombre_exemplaire, String imageContent) {
        this.name = name;
        this.author= author;
        this.nombre_exemplaire=nombre_exemplaire;
        this.imageContent=imageContent;
    }


}
