import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.Book;
import sn.esmt.mpisi2.repository.BookRepository;
import sn.esmt.mpisi2.service.BookService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;



@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)

@ExtendWith(SpringExtension.class)


public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;



    @Test
    void testSaveBook() {
        // Arrange
        byte[] imageContent = "Sample image content".getBytes();
        Book book = new Book(1,"Book Two", "Author", 5, imageContent);

        // Act
        bookService.save(book);

        // Assert
        // Book savedBook = bookService.getBookById(book.getId());
        assertNotNull(book);
        assertEquals("Book Two", book.getName());
        assertEquals("Author", book.getAuthor());
        assertEquals(5, book.getNombre_exemplaire());
        assertArrayEquals(imageContent,book.getImageContent());

        System.out.println("Livre enregistré avec succès : " + book);
    }



    @Test

    void testSaveBook_MissingField() {
        // Arrange

        BookService bookService = new BookService(bookRepository); // Mock ou Stub de BookRepository
        byte[] imageContent = "Sample image content".getBytes(); // Exemple de contenu d'image
        Book book = new Book(2, null, "Author", 5, imageContent); // Champ name manquant

        // Act & Assert
        MissingFieldException exception = assertThrows(MissingFieldException.class, () -> {
            bookService.save(book);
        });

        assertEquals("Veuillez renseigner tous les champs obligatoires : name, author, nombre_exemplaire", exception.getMessage());
    }

    @Test

    void testGetAllBook() {
        // Arrange
        byte[] imageContent1 = "Sample image content".getBytes();
        Book book1 = new Book(1,"Book Two", "Author", 5, imageContent1);

        byte[] imageContent2 = "Another sample image content".getBytes();
        Book book2 = new Book(2,"Book Three", "Author", 5, imageContent2);

        // Act
        bookService.save(book1);
        bookService.save(book2);

        // Assert
        assertNotNull(book1);
        assertNotNull(book2);

        // Vérifiez le premier livre
        assertEquals("Book Two", book1.getName());
        assertEquals("Author", book1.getAuthor());
        assertEquals(5, book1.getNombre_exemplaire());
        assertArrayEquals(imageContent1, book1.getImageContent());

        // Vérifiez le deuxième livre
        assertEquals("Book Three", book2.getName());
        assertEquals("Author", book2.getAuthor());
        assertEquals(5, book2.getNombre_exemplaire());
        assertArrayEquals(imageContent2, book2.getImageContent());

        // Act
        List<Book> books = bookRepository.findAll();

        // Assert
        assertEquals(2, books.size()); // Supposons qu'il y ait deux livres de test dans la base de données
        // Effectuez d'autres assertions selon les données de test préexistantes

        // Affichage sur la console
        System.out.println("Liste des livres récupérée : " + books);
    }


    @Test
    void testGetBookById() {
        // Arrange
        // Créez et sauvegardez plusieurs livres en base de données
        byte[] imageContent1 = "Sample image content".getBytes();
        Book book1 = new Book(1, "Book One", "Author A", 5, imageContent1);
        bookService.save(book1);

        byte[] imageContent2 = "Another sample image content".getBytes();
        Book book2 = new Book(2, "Book Two", "Author B", 3, imageContent2);
        bookService.save(book2);

//        byte[] imageContent3 = "Yet another sample image content".getBytes();
//        Book book3 = new Book(3, "Book Three", "Author C", 7, imageContent3);
//        bookService.save(book3);

        // Act
        // Récupérez le livre en utilisant son ID
        int existingBookId = 2; // ID d'un livre existant en base de données
        Book retrievedBook = bookService.getBookById(existingBookId);

        // Assert
        // Vérifiez que le livre récupéré correspond aux attentes
        assertNotNull(retrievedBook);
        assertEquals(existingBookId, retrievedBook.getId());
        assertEquals("Book Two", retrievedBook.getName());
        assertEquals("Author B", retrievedBook.getAuthor());
        assertEquals(3, retrievedBook.getNombre_exemplaire());
        assertArrayEquals(imageContent2, retrievedBook.getImageContent());

        // Affichage sur la console
        System.out.println("Livre récupéré avec succès : " + retrievedBook);
    }



    @Test
    void testDeleteBookById() {
        // Arrange
        byte[] imageContent = "Sample image content".getBytes(); // Exemple de contenu d'image
        Book bookToDelete = new Book(1,"Book to delete", "Author", 3, imageContent);
        bookService.save(bookToDelete);
        int bookIdToDelete = bookToDelete.getId();

        // Act
        bookService.deleteById(bookIdToDelete);

        // Assert
        Optional<Book> deletedBook = bookRepository.findById(bookIdToDelete);
        assertFalse(deletedBook.isPresent(), "Book should be deleted");

        // Vérifiez également si le contenu de l'image est conservé
        Book retrievedBook = deletedBook.orElse(null);
        if (retrievedBook != null) {
            assertArrayEquals(imageContent, retrievedBook.getImageContent(), "Image content should remain unchanged after deletion");
        }
    }


    @Test
    void testSearchByNameOrAuthor() {
        // Nettoyer la base de données avant chaque test

        // Arrange
        // Créez quelques livres pour simuler des données en base de données
        byte[] imageContent1 = "Sample image content".getBytes();
        Book book1 = new Book(1, "Book One", "Author A", 5, imageContent1);
        bookService.save(book1);

        byte[] imageContent2 = "Another sample image content".getBytes();
        Book book2 = new Book(2, "Book Two", "Author B", 3, imageContent2);
        bookService.save(book2);

        // Ajoutez d'autres livres au besoin

        String keyword = "Book";

        // Act
        // Effectuez la recherche en utilisant le mot-clé spécifié
        List<Book> result = bookService.searchByNameOrAuthor(keyword);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(book -> book.getName().toLowerCase().contains(keyword.toLowerCase()) || book.getAuthor().toLowerCase().contains(keyword.toLowerCase())));

        // Affichage des résultats
        System.out.println("Résultats de la recherche pour le mot-clé '" + keyword + "':");
        for (Book book : result) {
            System.out.println(book);
        }
    }

    @Test
    void testGetImageContentById() {
        // Arrange
        // Créez et sauvegardez plusieurs livres en base de données
        byte[] imageContent1 = "Sample image content".getBytes();
        Book book1 = new Book(1, "Book One", "Author A", 5, imageContent1);
        bookService.save(book1);

        byte[] imageContent2 = "Another sample image content".getBytes();
        Book book2 = new Book(2, "Book Two", "Author B", 3, imageContent2);
        bookService.save(book2);

        byte[] imageContent3 = "Yet another sample image content".getBytes();
        Book book3 = new Book(3, "Book Three", "Author C", 7, imageContent3);
        bookService.save(book3);

        int existingBookId = 3; // Supposez que l'ID d'un livre existant en base de données est 2

        // Act
        try {
            // Récupérez le contenu de l'image pour le livre avec l'ID spécifié
            byte[] imageContent = bookService.getImageContentById(existingBookId);

            // Assert
            assertNotNull(imageContent);
            assertTrue(imageContent.length > 0); // Vérifie si le contenu de l'image est non vide

            // Affichage sur la console
            System.out.println("Contenu de l'image récupéré avec succès pour le livre avec l'ID " + existingBookId);
        } catch (FileNotFoundException e) {
            // En cas d'erreur, affichez le message d'erreur
            fail("Erreur lors de la récupération du contenu de l'image : " + e.getMessage());
        }
    }

}
