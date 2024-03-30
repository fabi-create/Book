package sn.esmt.mpisi2.DTO;

import sn.esmt.mpisi2.model.MyBookList;
import sn.esmt.mpisi2.model.Reservation;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class EmprunteurDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;

    private String age;

    private String adresse;

    public EmprunteurDTO(String name, String email,String age,String adresse) {
        this.name = name;
        this.email=email;
        this.age=age;
        this.adresse=adresse;
    }

    public EmprunteurDTO(String name) {
        this.name = name;

    }

    public EmprunteurDTO(Long id,String name, String email,String age,String adresse) {
        this.id=id;
        this.name = name;
        this.email=email;
        this.age=age;
        this.adresse=adresse;
    }
    public EmprunteurDTO() {

    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Emprunteur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
