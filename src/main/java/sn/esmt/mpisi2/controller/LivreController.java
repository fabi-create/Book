package sn.esmt.mpisi2.controller;

import org.springframework.ui.Model;
import sn.esmt.mpisi2.DTO.BookDTO;
import sn.esmt.mpisi2.DTO.EmprunteurDTO;
import sn.esmt.mpisi2.DTO.MyBookListDTO;
import sn.esmt.mpisi2.DTO.ReservationDTO;
import sn.esmt.mpisi2.model.Book;
import sn.esmt.mpisi2.model.Emprunteur;
import sn.esmt.mpisi2.model.MyBookList;
import sn.esmt.mpisi2.model.Reservation;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.BookService;
import sn.esmt.mpisi2.service.EmprunteurService;
import sn.esmt.mpisi2.service.MyBookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.esmt.mpisi2.service.ReservationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class LivreController {

    private final UserRepository userRepository;
    private final BookService service;
    private final MyBookListService myBookService;

    @Autowired
    private EmprunteurService emprunteurService;

    @Autowired
    private ReservationService reservationService;


    public LivreController(UserRepository userRepository, BookService service, MyBookListService myBookService) {
        this.userRepository = userRepository;
        this.service = service;
        this.myBookService = myBookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> displayDashboard() {
        List<Book> list = service.getAllBook();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        List<Book> searchResults = service.searchByNameOrAuthor(keyword);
        return ResponseEntity.ok(searchResults);
    }



    @PostMapping("/add")
    public ResponseEntity<?> addLivre(@RequestBody BookDTO bookDTO) {
        try {
            Book book = new Book();
            book.setAuthor(bookDTO.getAuthor());
            book.setName(bookDTO.getName());
            book.setNombre_exemplaire(bookDTO.getNombre_exemplaire());
            Path path = Paths.get(bookDTO.getImageContent());
            byte[] imageBytes = Files.readAllBytes(path);
            book.setImageContent(imageBytes);
            service.save(book);
            return new ResponseEntity<>("Livre créé avec succès", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de la création du livre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLivre(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        try {
            Optional<Book> optionalBook = Optional.ofNullable(service.getBookById(Math.toIntExact(id)));

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                book.setAuthor(bookDTO.getAuthor());
                book.setName(bookDTO.getName());
                book.setNombre_exemplaire(bookDTO.getNombre_exemplaire());

                if (bookDTO.getImageContent() != null && !bookDTO.getImageContent().isEmpty()) {
                    Path path = Paths.get(bookDTO.getImageContent());
                    byte[] imageBytes = Files.readAllBytes(path);
                    book.setImageContent(imageBytes);
                }

                service.save(book);
                return new ResponseEntity<>("Livre mis à jour avec succès", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Livre non trouvé", HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour du livre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/book/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws Exception  {
        byte[] imageContent = service.getImageContentById(id);


        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContent);
    }

    @PostMapping("/saveMyBooks")
    public ResponseEntity<String> saveMyBooks(@RequestBody MyBookList myBookList) {
        myBookService.saveMyBooks(myBookList);
        return ResponseEntity.ok("My books saved successfully.");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful.");
    }

    @GetMapping("/book_register")
    public ResponseEntity<String> bookRegister() {
        return ResponseEntity.ok("Book register endpoint.");
    }


    @GetMapping("/SearchBook/{id}")
    public ResponseEntity<Book> editBook(@PathVariable("id") int id) {
        Book b = service.getBookById(id);
        if (b == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(b);
    }

    @PostMapping("/mylist/{id}")
    public ResponseEntity<MyBookList> getMyList(@PathVariable("id") int id) {
        Book b = service.getBookById(id);
        if (b == null) {
            return ResponseEntity.notFound().build();
        }

        MyBookList myBookList = new MyBookList();
        myBookList.setCustomId(id);
        myBookList.setName(b.getName());
        myBookList.setAuthor(b.getAuthor());
        myBookList.setDate_emprunt(Date.valueOf(LocalDate.now()));
        myBookList.setDate_retour_prevue(Date.valueOf(LocalDate.now().plusDays(14)));

        int nombreExemplaire = b.getNombre_exemplaire();
        if (nombreExemplaire > 0) {
            b.setNombre_exemplaire(nombreExemplaire - 1);
            service.save(b);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Pas assez d'exemplaires disponibles
        }

        String username = returnUsername();
        myBookList.setNom_emprunteur(username);

        myBookService.saveMyBooks(myBookList);

        return ResponseEntity.status(HttpStatus.CREATED).body(myBookList);
    }


    @GetMapping("/my_book_lists")
    public ResponseEntity<List<MyBookListDTO>> getAllMyBookListDTO() {
        List<MyBookListDTO> myBookListDTOs = myBookService.getAllMyBookListDTO();
        if (myBookListDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(myBookListDTOs);
    }


    @DeleteMapping("/deleteMyList/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMyList(@PathVariable("id") int id) {
        myBookService.deleteById(id);
        return ResponseEntity.ok().body("L'emprunt a été supprimée avec succès.");
    }



    @PutMapping("/editMyList/{id}")
    @ResponseBody
    public ResponseEntity<String> editMyList(@PathVariable("id") int id, @RequestBody MyBookList myBookList) {
        MyBookList existingMyBookList = myBookService.getMyBookListById(id);
        if (existingMyBookList == null) {
            return ResponseEntity.notFound().build(); // Renvoyer une réponse 404 Not Found si la liste n'est pas trouvée
        }

        // Mettre à jour les détails de la liste de livres avec les nouvelles valeurs

        existingMyBookList.setPenalite(myBookList.getPenalite());

        existingMyBookList.setDate_retour(myBookList.getDate_retour());
        existingMyBookList.setEmprunteur(myBookList.getEmprunteur());

        // Mettre à jour d'autres champs si nécessaire

        myBookService.saveMyBooks(existingMyBookList);
        return ResponseEntity.ok().body("La liste de livres a été modifiée avec succès.");
    }


    @PostMapping ("/myreservation/{id}")
    public ResponseEntity<String> getMyReservation(@PathVariable("id") int id, Model model) {
        Book b = service.getBookById(id);

        // Vérifier si des exemplaires du livre sont disponibles
        if (b.getNombre_exemplaire() > 0) {
            // Gérer le cas où des exemplaires sont encore disponibles
            // Vous pouvez renvoyer un message d'erreur à l'utilisateur ou rediriger vers une page spécifique
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Des exemplaires du livre sont pas encore disponibles.");
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
        return ResponseEntity.status(HttpStatus.CREATED).body("Réservation créée avec succès.");
    }





    @GetMapping("/my_reservation_lists")
    public ResponseEntity<List<ReservationDTO>> getAllReservationDTO() {
        List<ReservationDTO> reservationDTOS = reservationService.getAllReservationDTO();
        if (reservationDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(reservationDTOS);
    }


    @PostMapping("/updateEmprunteurReservation/{id}")
    @ResponseBody
    public ResponseEntity<String> updateEmprunteurReservation(@PathVariable("id") int id,
                                                   @RequestParam("emprunteurId") int emprunteurId) {
        reservationService.updateEmprunteurId(id, emprunteurId);
        return ResponseEntity.ok("Emprunteur mis à jour avec succès pour la réservation avec l'ID : " + id);
    }



    //Revenir sur la requête suivante ci possible
    @PutMapping("/UpdateReservationInfo/{id}")
    @ResponseBody
    public ResponseEntity<String> edit(@PathVariable("id") int id, @RequestBody Reservation reservation) {
        Reservation existingReservation = reservationService.getReservationById(id);
        if (existingReservation == null) {
            return ResponseEntity.notFound().build(); // Renvoyer une réponse 404 Not Found si la réservation n'est pas trouvée
        }


        existingReservation.setName(reservation.getName());
        existingReservation.setAuthor(reservation.getAuthor());
        existingReservation.setNom_emprunteur(reservation.getNom_emprunteur());
        existingReservation.setEmprunteur(reservation.getEmprunteur());
        // Mettre à jour d'autres champs si nécessaire

        reservationService.saveReservation(existingReservation);
        return ResponseEntity.ok().body("La réservation a été modifiée avec succès.");
    }

  //Revenir sur la requête suivante ci possible
    @PutMapping("/updatedReservation/{id}")
    public ResponseEntity<String> updatedReservation(@PathVariable("id") int id,
                                                     @RequestParam("emprunteurId") long emprunteurId,
                                                     @RequestBody Reservation updatedReservation) {
        reservationService.updateReservation(id, emprunteurId, updatedReservation);
        return ResponseEntity.ok("Réservation mise à jour avec succès avec l'ID : " + id);
    }

    @DeleteMapping("/deleteMyReservationList/{id}")
    public ResponseEntity<String> deleteMyReservationList(@PathVariable("id") int id) {
        reservationService.deleteById(id);
        return ResponseEntity.ok("Réservation supprimée avec succès avec l'ID : " + id);
    }


    //j


    @PostMapping("/updateEmprunteur/{id}")
    @ResponseBody
    public ResponseEntity<String> updateEmprunteur(@PathVariable("id") int id,
                                                   @RequestParam("emprunteurId") int emprunteurId) {
        myBookService.updateEmprunteurId(id, emprunteurId);
        return ResponseEntity.ok().body("Emprunteur mis à jour avec succès.");
    }

    @PostMapping("/updateDateRetour/{id}")
    @ResponseBody
    public ResponseEntity<String> updateDateRetour(@PathVariable("id") int id,
                                                   @RequestParam("dateRetour") Date dateRetour) {
        myBookService.updateDateRetour(id, dateRetour);
        return ResponseEntity.ok().body("Date de retour mise à jour avec succès.");
    }
    private String returnUsername() {
        // Implémentez la logique pour obtenir le nom d'utilisateur approprié
        return "utilisateur_de_test";
    }




    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int id) {
        // Vérifier si le livre existe avant de le supprimer
        Optional<Book> optionalBook = Optional.ofNullable(service.getBookById(id));

        if (optionalBook.isPresent()) {
            // Supprimer le livre de la table Book
            service.deleteById(id);
            // Supprimer l'entrée correspondante dans MyBookList en fonction de l'ID du livre
            myBookService.deleteByBookId(id);
            return ResponseEntity.ok("Livre supprimé avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre non trouvé");
        }
    }


    @GetMapping("/emprunteurs")
    public List<EmprunteurDTO> getAllEmprunteurs() {
        List<EmprunteurDTO> emprunteursDTO = emprunteurService.getAllEmprunteurDTO();
        return emprunteursDTO;
    }

    @PostMapping("/addEmprunteur")
    public ResponseEntity<String> addEmprunteur(@RequestBody Emprunteur emprunteur) {
        try {
            emprunteurService.save(emprunteur);
            return ResponseEntity.status(HttpStatus.CREATED).body("Emprunteur ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de l'emprunteur");
        }
    }

    @PutMapping("/emprunteur/{id}")
    public ResponseEntity<String> updateEmprunteur(@PathVariable("id") long id, @RequestBody Emprunteur emprunteur) {
        try {
            emprunteur.setId(id);
            emprunteurService.save(emprunteur);
//            return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.CREATED).body("Emprunteur modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de l'emprunteur");
        }
    }

    @DeleteMapping("/emprunteur/{id}")
    public ResponseEntity<String> deleteEmprunteur(@PathVariable("id") long id) {
        try {
            emprunteurService.deleteById(id);
//            return ResponseEntity.noContent().build();

            return ResponseEntity.status(HttpStatus.CREATED).body("Emprunteur supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de l'emprunteur");
        }
    }




}
