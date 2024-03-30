package sn.esmt.mpisi2.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)
public class DashboardControllerIntegrationTest {



    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private MyBookListService myBookListService;

    @MockBean
    private EmprunteurService emprunteurService;

    @MockBean
    private ReservationServiceImpl reservationService;



    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Mock  // Annotation pour indiquer que c'est un mock
    private UserRepository userRepo;  // Injection du repository

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

//    @MockBean
//    private MyBookListService myBookListService;
//
//    @MockBean
//    private EmprunteurService emprunteurService;
//
//    @MockBean
//    private ReservationService reservationService;

//    @Test
//    @WithMockUser(username = "test@example.com", authorities = {"USER"})
//    public void testDisplayDashboard() throws Exception {
//        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
//        when(bookService.getAllBook()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("dashboard"));
//    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testDisplayDashboard() throws Exception {
        // Créez un utilisateur avec le rôle "USER"
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(new Role("USER")); // Assurez-vous que la classe Role dispose d'un constructeur prenant un rôle en paramètre
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Définissez le comportement de bookService.getAllBook() pour renvoyer une liste vide, car c'est ce que vous attendez dans ce test
        when(bookService.getAllBook()).thenReturn(Collections.emptyList());

        // Effectuez la requête GET vers /dashboard
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Vérifiez que le statut de la réponse est OK (200)
                .andExpect(MockMvcResultMatchers.view().name("dashboard")); // Vérifiez que la vue renvoyée est "dashboard"
    }


    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testSearchBooks() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
        when(bookService.searchByNameOrAuthor("example")).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/search")
                        .param("keyword", "example"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("dashboard"));
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testAddBook() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/dashboard/save")
                        .file(new MockMultipartFile("file", "test.txt", "text/plain", "file content".getBytes()))
                        .param("name", "Book Name")
                        .param("author", "Author Name")
                        .param("nombre_exemplaire", "232")) // Correction ici : un seul appel à param pour chaque paramètre
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard"));
    }


    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testAddMyBooks() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User( "test@example.com", "password123"));

        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/saveMyBooks")
                        .param("id", "1")
                        .param("name", "Book Name")
                        .param("author", "Author Name")
                        .param("date_emprunt", String.valueOf(Date.valueOf(LocalDate.now())))
                        .param("date_retour_prevue", String.valueOf(Date.valueOf(LocalDate.now().plusDays(14))))
                        .param("nom_emprunteur", "Test User"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/my_books"));
    }

//    @Test
//    @WithMockUser(username = "test@example.com", authorities = {"USER"})
//    public void testAddMyBooks() throws Exception {
//        // Créer un objet Book avec un ID spécifique et un nombre d'exemplaires initial
//        Book book = new Book();
//        book.setId(1);
//        book.setName("Book Name");
//        book.setAuthor("Author Name");
//        book.setNombre_exemplaire(5); // Nombre d'exemplaires initial
//
//        // Configurer le comportement du repository pour retourner le livre créé lorsque findByEmail est appelé
//        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
//
//        // Configurer le comportement du service pour retourner le livre créé lorsque getBookById est appelé avec l'ID spécifique
//        when(bookService.getBookById(1)).thenReturn(book);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/saveMyBooks")
//                        .param("id", "1")
//                        .param("name", "Book Name")
//                        .param("author", "Author Name")
//                        .param("date_emprunt", String.valueOf(Date.valueOf(LocalDate.now())))
//                        .param("date_retour_prevue", String.valueOf(Date.valueOf(LocalDate.now().plusDays(14))))
//                        .param("nom_emprunteur", "Test User"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/my_books"));
//
//        // Vérifier que la méthode save du service bookService a été appelée avec le bon objet Book
//        verify(bookService, times(1)).save(book);
//    }




    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testGetMyBookDetails() throws Exception {
        MyBookList myBookList = new MyBookList();
        myBookList.setId(1);
        myBookList.setName("Book Name");
        myBookList.setAuthor("Author Name");
        myBookList.setDate_emprunt(Date.valueOf(LocalDate.now()));
        myBookList.setDate_retour_prevue(Date.valueOf(LocalDate.now().plusDays(14)));
        myBookList.setNom_emprunteur("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
        when(myBookListService.getMyBookListById(1)).thenReturn(myBookList);

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/my_book/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("myBookDetails"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myBookList"))
                .andExpect(MockMvcResultMatchers.model().attribute("myBookList", myBookList));
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testGetMyBookReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setName("Book Name");
        reservation.setAuthor("Author Name");
        reservation.setNom_emprunteur("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
        when(reservationService.getReservationById(1)).thenReturn(reservation);

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/my_reservation/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("MyBookReservation"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("reservation"))
                .andExpect(MockMvcResultMatchers.model().attribute("reservation", reservation));
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testGetMyBooks() throws Exception {
        MyBookList myBookList = new MyBookList();
        myBookList.setId(1);
        myBookList.setName("Book Name");
        myBookList.setAuthor("Author Name");
        myBookList.setDate_emprunt(Date.valueOf(LocalDate.now()));
        myBookList.setDate_retour_prevue(Date.valueOf(LocalDate.now().plusDays(14)));
        myBookList.setNom_emprunteur("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User( "test@example.com", "password123"));
        when(myBookListService.getAllMyBooks()).thenReturn(Collections.singletonList(myBookList));

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/my_books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("myBooks"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allmyBooklist"))
                .andExpect(MockMvcResultMatchers.model().attribute("allmyBooklist", Collections.singletonList(myBookList)));
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    public void testGetMyReservations() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setName("Book Name");
        reservation.setAuthor("Author Name");
        reservation.setNom_emprunteur("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User("test@example.com", "password123"));
        when(reservationService.getAllReservation()).thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/my_reservations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("myReservations"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allmyReservationlist"))
                .andExpect(MockMvcResultMatchers.model().attribute("allmyReservationlist", Collections.singletonList(reservation)));
    }


}
