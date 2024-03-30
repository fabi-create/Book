package sn.esmt.mpisi2.model;


import javax.persistence.*;

import java.util.List;

@Entity
public class Emprunteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;

    private String age;

    private String adresse;



    @OneToMany(mappedBy = "emprunteur")
    private List<MyBookList> myBookList;

    @OneToMany(mappedBy = "emprunteur")
    private List<Reservation> reservations;

    public Emprunteur(String name, String email,String age,String adresse) {
        this.name = name;
        this.email=email;
        this.age=age;
        this.adresse=adresse;
    }

    public Emprunteur(String name) {
        this.name = name;

    }

    public Emprunteur(Long id,String name, String email,String age,String adresse) {
        this.id=id;
        this.name = name;
        this.email=email;
        this.age=age;
        this.adresse=adresse;
    }
    public Emprunteur() {

    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<MyBookList> getMyBookList() {
        return myBookList;
    }

    public void setMyBookList(List<MyBookList> myBookList) {
        this.myBookList = myBookList;
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

//    @Override
//    public String toString() {
//        return name;
//    }


}