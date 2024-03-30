package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.DTO.EmprunteurDTO;
import sn.esmt.mpisi2.DTO.MyBookListDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MyBookListService {

	@Autowired
	private MyBookRepository mybook;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private EmprunteurRepository emprunteurRepository;


	// Autres injections de dépendances...

	public List<Emprunteur> getAllEmprunteurs() {
		return emprunteurRepository.findAll();
	}

	public void saveMyBooks(MyBookList myBookList) {
		mybook.save(myBookList);
	}



	public List<MyBookList> getAllMyBooks(){
		return mybook.findAll();
	}

	public void deleteByBookId(int custom_id) {
		// Récupérer les entrées de MyBookList liées au livre avec l'ID spécifié
		List<MyBookList> myBookList = mybook.findByCustomId(custom_id);

		// Supprimer chaque entrée de MyBookList
		myBookList.forEach(mybook::delete);
	}

	public MyBookList getMyBookListById(int id) {
		return mybook.findById(id).get();
	}



	public void deleteById (int id) {
		if(Objects.nonNull(id)){
			mybook.deleteById(id);
		}
	}

	@Transactional
	public void updateDateRetour(int id, Date dateRetour) {
		MyBookList myBookList = mybook.findById(id).orElse(null);
		if (myBookList != null) {
			Date dateEmprunt = myBookList.getDate_emprunt();
			if (dateRetour.before(dateEmprunt)) {
				throw new IllegalArgumentException("La date de retour ne peut pas être antérieure à la date d'emprunt.");
			}

			myBookList.setDate_retour(dateRetour);

			// Calcul de la pénalité
			long penalite = calculatePenalite(myBookList.getDate_retour_prevue(), dateRetour);
			myBookList.setPenalite(penalite);

			mybook.save(myBookList);


			// Incrémenter le nombre d'exemplaires de Book
			Book book = bookRepository.findById(myBookList.getCustomId()).orElse(null);
			if (book != null) {
				int nombreExemplaire = book.getNombre_exemplaire();
				book.setNombre_exemplaire(nombreExemplaire + 1);
				bookRepository.save(book);
			}
		} else {
			// Gérer le cas où aucun élément avec l'ID spécifié n'est trouvé
		}
	}


	private long calculatePenalite(Date dateRetourPrevue, Date dateRetour) {
		LocalDate dateRetourPrevueLD = dateRetourPrevue.toLocalDate();
		LocalDate dateRetourLD = dateRetour.toLocalDate();

		// Calculer la différence en jours entre la date de retour et la date de retour prévue
		long differenceJours = Duration.between(dateRetourPrevueLD.atStartOfDay(), dateRetourLD.atStartOfDay()).toDays();

		// Supposons que la pénalité est de 10 unités par jour de retard
		long tauxPenalite = 10;

		// Calculer la pénalité
		return differenceJours * tauxPenalite;
	}


	@Transactional
	public void updateEmprunteurId(int id, long emprunteurId) {
		MyBookList myBookList = mybook.findById(id).orElse(null);
		Emprunteur emprunteur = emprunteurRepository.findById(emprunteurId).orElse(null); // Chargez l'entité Emprunteur à partir de la base de données
		if (myBookList != null && emprunteur != null) {
			myBookList.setEmprunteur(emprunteur); // Associez l'emprunteur à l'entité MyBookList
			mybook.save(myBookList);
		} else {
			// Gérer le cas où aucun élément avec l'ID spécifié n'est trouvé
		}
	}

//	public List<MyBookList> getAllMyBooks(){
//		return mybook.findAll();
//	}

	public List<MyBookListDTO> getAllMyBookListDTO() {
		List<MyBookList> myBookLists = mybook.findAll();
		List<MyBookListDTO> MyBookListsDTO = convertMyBookListToDTO(myBookLists);
		return MyBookListsDTO;
	}

	private List<MyBookListDTO> convertMyBookListToDTO(List<MyBookList> myBookLists) {
		List<MyBookListDTO> MyBookListsDTO = new ArrayList<>();
		for (MyBookList myBookList : myBookLists) {
			MyBookListsDTO.add(convertToDTO(myBookList));
		}
		return MyBookListsDTO;
	}

	private MyBookListDTO convertToDTO(MyBookList myBookList) {
		MyBookListDTO myBookListDTO = new MyBookListDTO();
		myBookListDTO.setId(myBookList.getId());
		myBookListDTO.setCustomId(myBookList.getCustomId());
		myBookListDTO.setName(myBookList.getName());
		myBookListDTO.setAuthor(myBookList.getAuthor());
		myBookListDTO.setNom_emprunteur(myBookList.getNom_emprunteur());
		myBookListDTO.setDate_emprunt(myBookList.getDate_emprunt());
		myBookListDTO.setDate_retour_prevue(myBookList.getDate_retour_prevue());
		myBookListDTO.setDate_retour(myBookList.getDate_retour());
		myBookListDTO.setPenalite(myBookList.getPenalite());
		// Remplacer emprunteur par l'ID de l'emprunteur ou ajuster en conséquence
		myBookListDTO.setEmprunteur(myBookList.getEmprunteur().getId());
		return myBookListDTO;
	}



}
