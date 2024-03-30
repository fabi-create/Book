package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bRepo;
//	@Autowired
//	private BookRepository bRepo;

	@Autowired
	MyBookRepository mRepo;

	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bRepo = bookRepository;
	}


//	public void save(Book b) {
//		bRepo.save(b);
//	}


	public void save(Book b) {
		if (b.getName() == null || b.getAuthor() == null || b.getNombre_exemplaire()== 0 || b.getImageContent()== null) {
			throw new MissingFieldException("Veuillez renseigner tous les champs obligatoires : name, author, nombre_exemplaire");
		}
		bRepo.save(b);
	}


//	public Book save(Book book) {
//		// Vérifie si le livre existe déjà dans la base de données
//		Optional<Book> existingBookOptional = bRepo.findById(book.getId());
//
//		if (existingBookOptional.isPresent()) {
//			// Le livre existe déjà, vous pouvez choisir de gérer cette situation comme vous le souhaitez
//			// Par exemple, lever une exception ou fusionner les informations du nouveau livre avec l'ancien
//			// Pour cet exemple, nous lèverons simplement une exception.
//			throw new IllegalArgumentException("Un livre avec cet ID existe déjà : " + book.getId());
//		}
//
//		// Le livre n'existe pas encore, nous pouvons donc le sauvegarder
//		return bRepo.save(book);
//	}


	public List<Book> getAllBook(){
		return bRepo.findAll();
	}

	public Book getBookById(int id) {
		return bRepo.findById(id).get();
	}
	public void deleteById(int id) {
		bRepo.deleteById(id);
	}

	public List<Book> searchByNameOrAuthor(String keyword) {
		return bRepo.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
	}


	public byte[] getImageContentById(Integer id) throws FileNotFoundException {
		Optional<Book> livreOptional = bRepo.findById(id);

		if (livreOptional.isPresent()) {
			Book livre = livreOptional.get();
			return livre.getImageContent();

		} else {
			// Gérer le cas où aucun livre avec l'ID spécifié n'est trouvé
			throw new FileNotFoundException("Livre non trouvé avec l'ID : " + id);
		}
	}




}
