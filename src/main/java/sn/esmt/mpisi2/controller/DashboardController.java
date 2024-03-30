package sn.esmt.mpisi2.controller;

import sn.esmt.mpisi2.DTO.UserWithRoleDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookService service;

	@Autowired
	private MyBookListService myBookService;

	@Autowired
	private EmprunteurService emprunteurService;

	@Autowired
	private ReservationService reservationService;

//	private final BookService service;
//	private final MyBookListService myBookService; // Ajoutez cette ligne
//
//	// Constructeur prenant les services nécessaires pour l'injection de dépendance
//	public DashboardController(BookService service, MyBookListService myBookService) {
//		this.service = service;
//		this.myBookService = myBookService; // Initialisez myBookService
//	}



	@GetMapping
	public String displayDashboard(Model model) {
		List<Book> list = service.getAllBook();
		String user = returnUsername();
		model.addAttribute("userDetails", user);
		model.addAttribute("book", list);
		return "dashboard";
	}



	@GetMapping ("/search")
	public String searchBooks(@RequestParam String keyword, Model model) {
		List<Book> searchResults = service.searchByNameOrAuthor(keyword);
		String user = returnUsername();
		model.addAttribute("userDetails", user);
		model.addAttribute("book", searchResults);
		return "dashboard";
	}

	@GetMapping("/")
	public String dashboard() {
		return "dashboard";
	}

//	private String returnUsername() {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
//		User users = userRepository.findByEmail(user.getUsername());
//		return users.getName();
//	}

	private String returnUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			// Si l'authentification est null ou non authentifiée, retournez un username par défaut
			return "anonymous";
		}

		UserDetails user = (UserDetails) authentication.getPrincipal();
		User users = userRepository.findByEmail(user.getUsername());
		if (users != null) {
			return users.getName();
		} else {
			// Gérez le cas où l'utilisateur n'est pas trouvé dans la base de données
			return "unknown";
		}
	}


	@PreAuthorize("hasAuthority('MANAGER')")
	@GetMapping("/book_register")
	public String bookRegister() {
		return "bookRegister";
	}

	@GetMapping("/test")
	public String test() {
		return "test";
	}

	@PostMapping("/save")
	public String addBook(@ModelAttribute Book book, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] imageContent = file.getBytes();
				book.setImageContent(imageContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		service.save(book);
		return "redirect:/dashboard"; // Rediriger vers la page de tableau de bord après l'enregistrement du livre
	}


	@GetMapping("/book/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable int id) throws FileNotFoundException {

		byte[] imageContent = service.getImageContentById(id);


		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContent);
	}

	@PostMapping("/saveMyBooks")
	public String saveMyBooks(@ModelAttribute MyBookList myBookList) {
		myBookService.saveMyBooks(myBookList); // Appeler la méthode pour enregistrer MyBookList
		return "redirect:/dashboard/my_books"; // Rediriger vers une page de confirmation ou de liste de livres
	}




	@GetMapping("/my_book/{id}")
	public String getMyBookDetails(@PathVariable("id") int id, Model model) {
		MyBookList myBookList = myBookService.getMyBookListById(id);
		List<Emprunteur> emprunteurs = myBookService.getAllEmprunteurs();
		model.addAttribute("myBookList", myBookList);
		model.addAttribute("emprunteurs", emprunteurs);
		return "myBookDetails"; // Assurez-vous d'avoir une vue nommée "myBookDetails" pour afficher les détails de l'enregistrement
	}


	@GetMapping("/my_reservation/{id}")
	public String getMyBookReservation(@PathVariable("id") int id, Model model) {
		Reservation reservation = reservationService.getReservationById(id);
		List<Emprunteur> emprunteurs = myBookService.getAllEmprunteurs();
		model.addAttribute("reservation", reservation);
		model.addAttribute("emprunteurs", emprunteurs);
		return "MyBookReservation";
	}





	@RequestMapping("/mylist/{id}")
	public String getMyList(@PathVariable("id") int id, Model model) {
		Book b = service.getBookById(id);

//		if (b.getNombre_exemplaire() == 0) {
//
//			return "redirect:/error-page"; // Exemple de redirection vers une page d'erreur
//		}


		if (b != null) {
			// Vérifier si des exemplaires du livre sont disponibles
			if (b.getNombre_exemplaire() == 0) {
				// Gérer le cas où des exemplaires sont encore disponibles
				// Vous pouvez renvoyer un message d'erreur à l'utilisateur ou rediriger vers une page spécifique
				return "redirect:/error-page"; // Exemple de redirection vers une page d'erreur
			}
		MyBookList myBookList = new MyBookList();
		myBookList.setCustomId(id);
		myBookList.setName(b.getName());
		myBookList.setAuthor(b.getAuthor());

		// Mettre à jour les champs requis
		myBookList.setDate_emprunt(Date.valueOf(LocalDate.now())); // Exemple de date d'emprunt actuelle
		myBookList.setDate_retour_prevue(Date.valueOf(LocalDate.now().plusDays(14))); // Exemple de date de retour prévue dans 14 jours

		// Soustraire un exemplaire du nombre total d'exemplaires disponibles du livre
		int nombreExemplaire = b.getNombre_exemplaire();
		b.setNombre_exemplaire(nombreExemplaire - 1);
		service.save(b); // Mettre à jour le nombre d'exemplaires disponibles dans la base de données

		// Ajouter le nom de l'utilisateur connecté
		String username = returnUsername();
		myBookList.setNom_emprunteur(username);

		myBookService.saveMyBooks(myBookList);
		List<Emprunteur> emprunteurs = myBookService.getAllEmprunteurs();
		model.addAttribute("emprunteurs", emprunteurs);

		// Rediriger vers une vue spécifique pour afficher les détails de l'enregistrement créé
		return "redirect:/dashboard/my_book/" + myBookList.getId(); // Assurez-vous d'avoir une vue nommée "details" pour afficher les détails de l'enregistrement

		} else {
			// Gérer le cas où aucun livre avec l'ID spécifié n'est trouvé
			// Vous pouvez renvoyer un message d'erreur à l'utilisateur ou rediriger vers une page spécifique
			return "redirect:/error-page"; // Exemple de redirection vers une page d'erreur
		}
		}



	@RequestMapping("/myreservation/{id}")
	public String getMyReservation(@PathVariable("id") int id, Model model) {
		Book b = service.getBookById(id);

		// Vérifier si des exemplaires du livre sont disponibles
		if (b.getNombre_exemplaire() > 0) {
			// Gérer le cas où des exemplaires sont encore disponibles
			// Vous pouvez renvoyer un message d'erreur à l'utilisateur ou rediriger vers une page spécifique
			return "redirect:/error-page"; // Exemple de redirection vers une page d'erreur
		}

		Reservation reservation = new Reservation ();
		reservation.setCustomId(id);
		reservation.setName(b.getName());
		reservation.setAuthor(b.getAuthor());

		// Ajouter le nom de l'utilisateur connecté
		String username = returnUsername();
		reservation.setNom_emprunteur(username);

		reservationService.saveReservation(reservation);
		List<Emprunteur> emprunteurs = reservationService.getAllEmprunteurs();
		model.addAttribute("emprunteurs", emprunteurs);

		// Rediriger vers une vue spécifique pour afficher les détails de l'enregistrement créé
		return "redirect:/dashboard/my_reservation/" + reservation.getId(); // Assurez-vous d'avoir une vue nommée "details" pour afficher les détails de l'enregistrement
	}



	@PreAuthorize("hasAuthority('MANAGER')")
	@RequestMapping("/editBook/{id}")
	public String editBook(@PathVariable("id") int id,Model model) {
		Book b=service.getBookById(id);
		model.addAttribute("book",b);
		return "bookEdit";
	}

	@PreAuthorize("hasAuthority('MANAGER')")
	@RequestMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		// Supprimer le livre de la table Book
		service.deleteById(id);

		// Supprimer l'entrée correspondante dans MyBookList en fonction de l'ID du livre
		myBookService.deleteByBookId(id);

		return "redirect:/dashboard";
	}


//	@GetMapping("/my_Books")
//	public String getAllMyBooks(Model model){
//		model.addAttribute("allmyBooklist",myBookService.getAllMyBooks());
//		return "myBooks";
//	}

		@GetMapping("/my_books")
	public String getMyBooks(Model model) {
		List<MyBookList> myBooks = myBookService.getAllMyBooks();
		// Récupérer la liste des emprunteurs

		model.addAttribute("allmyBooklist", myBooks);

		return "myBooks";
	}

	@GetMapping("/my_reservations")
	public String getMyRservations(Model model) {
		List<Reservation> myReservations = reservationService.getAllReservation();
		// Récupérer la liste des emprunteurs

		model.addAttribute("allmyReservationlist", myReservations);

		return "myReservations";
	}
	/// operation concernant les emprunteurs
	@GetMapping("/emprunteur")
	public String viewHomePage(Model model){
		model.addAttribute("allemprunteurlist", emprunteurService.getAllEmprunteur());
		return "emprunteur";
	}
	@GetMapping("/addEmprunteur")
	public String addNewEmployee(Model model){
		Emprunteur emprunteur = new Emprunteur();
		model.addAttribute("emprunteur", emprunteur);
		return "addEmprunteur";
	}

	@PostMapping("/saveEmprunteur")
	public String saveEmprunteur(@ModelAttribute("emprunteur") Emprunteur emprunteur){
		emprunteurService.save(emprunteur);
		return "redirect:/dashboard/emprunteur";
	}

	@GetMapping("updateform/{id}")
	public String updateForm(@PathVariable(value = "id") long id, Model model){
		Emprunteur emprunteur = emprunteurService.getById(id);
		model.addAttribute("emprunteur", emprunteur);
		return "update";
	}

	@GetMapping("/delete/{id}")
	public String deleteById(@PathVariable(value = "id") long id){
		emprunteurService.deleteById(id);
		return "redirect:/dashboard/emprunteur";
	}



	@PostMapping("/saveReservation")
	public String saveReservation(@ModelAttribute("reservation") Reservation reservation){
		reservationService.save(reservation);
		return "redirect:/dashboard/reservation";
	}

}
