package sn.esmt.mpisi2.controller;


import sn.esmt.mpisi2.DTO.UserWithRoleDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;


@Controller
public class MyBookListController {

	@Autowired
	private MyBookListService service;


	@Autowired
	private EmprunteurService emprunteurService;


	@RequestMapping("/deleteMyList/{id}")
	public String deleteMyList(@PathVariable("id") int id) {
		service.deleteById(id);
		return "redirect:/dashboard/my_books";
	}

	@RequestMapping("/editMyList/{id}")
	public String editMyList(@PathVariable("id") int id, Model model) {
		MyBookList b=service.getMyBookListById(id);
		model.addAttribute("myBookList",b);
		return "MyBookListEdit";
		//return "redirect:/my_books";
	}

	@PostMapping("/updateEmprunteur/{id}")
	public String updateEmprunteur(@PathVariable("id") int id,
								   @RequestParam("emprunteurId") int emprunteurId) {
		service.updateEmprunteurId(id, emprunteurId);
		return "redirect:/dashboard/my_books"; // Redirigez vers une page appropriée après la mise à jour
	}


	@PostMapping("/{id}/updateDateRetour")
	public String updateDateRetour(@PathVariable("id") int id, @RequestParam("dateRetour") Date dateRetour) {
		service.updateDateRetour(id, dateRetour);
		return "redirect:/dashboard/my_books"; // Rediriger vers la page de liste de vos livres après la mise à jour de la date de retour
	}


//	@PostMapping("/{id}/updateDateRetour")
//	public String updateDateRetour(@PathVariable("id") int id, @RequestParam("dateRetour") Date dateRetour) {
//		MyBookList myBookList = service.getMyBookListById(id); // Récupérer l'objet MyBookList par son ID
//		myBookList.setDate_retour(dateRetour); // Mettre à jour la date de retour
//		// Autres mises à jour si nécessaire
//		service.saveMyBooks(myBookList); // Enregistrer les modifications dans la base de données
//		return "redirect:/dashboard/my_books"; // Rediriger vers la page de liste de vos livres après la mise à jour de la date de retour
//	}




}
