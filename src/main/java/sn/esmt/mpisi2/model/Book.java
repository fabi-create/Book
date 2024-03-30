package sn.esmt.mpisi2.model;


import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private String name;
	private String author;

	private int nombre_exemplaire;

	@Lob
	private byte[] imageContent;



	public byte[] getImageContent() {
		return imageContent;
	}

	public void setImageContent(byte[] imageContent) {
		this.imageContent = imageContent;
	}


	public Book(int id, String name, String author, int nombre_exemplaire) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.nombre_exemplaire = nombre_exemplaire;

	}

	public Book(String name, String author, int nombre_exemplaire, byte[] imageContent) {
		super();

		this.name = name;
		this.author = author;
		this.nombre_exemplaire = nombre_exemplaire;
		this.imageContent=imageContent;

	}

	public Book(String name, String author, int nombre_exemplaire) {
		super();

		this.name = name;
		this.author = author;
		this.nombre_exemplaire = nombre_exemplaire;


	}

	public Book(int id, String name, String author, int nombre_exemplaire, byte[] imageContent) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.nombre_exemplaire = nombre_exemplaire;
		this.imageContent=imageContent;

	}


	public Book() {

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

	public int getNombre_exemplaire() {
		return nombre_exemplaire;
	}

	public void setNombre_exemplaire(int nombre_exemplaire) {
		this.nombre_exemplaire = nombre_exemplaire;
	}


	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", name='" + name + '\'' +
				", author='" + author + '\'' +
				", nombre_exemplaire=" + nombre_exemplaire + '\'' +
				", imageContent=" + imageContent +

				'}';
	}
}
