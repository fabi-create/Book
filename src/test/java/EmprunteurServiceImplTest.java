
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.model.Emprunteur;
import sn.esmt.mpisi2.repository.EmprunteurRepository;
import sn.esmt.mpisi2.service.EmprunteurServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)
public class EmprunteurServiceImplTest {


    @Autowired
    private EmprunteurServiceImpl emprunteurServiceImpl;

    @Autowired
    private EmprunteurRepository emprunteurRepository;



    @Test
    void testSave() {
        // Arrange

        Emprunteur emprunteur = new Emprunteur("toto","toto@gmail.com","20","TotoVille");
        // Act
        emprunteurServiceImpl.save(emprunteur);

        // Retrieve the saved Emprunteur from the database
        Emprunteur savedEmprunteur =emprunteurServiceImpl.getById(emprunteur.getId());

        // Assert
        assertNotNull(savedEmprunteur);
        assertEquals(emprunteur.getName(), savedEmprunteur.getName());
        assertEquals(emprunteur.getEmail(), savedEmprunteur.getEmail());
        assertEquals(emprunteur.getAge(), savedEmprunteur.getAge());
        assertEquals(emprunteur.getAdresse(), savedEmprunteur.getAdresse());
        // Affichage sur la console
        System.out.println("Emprunteur enregistré avec succès : " + savedEmprunteur);
    }
    @Test
    void testGetAllEmprunteur() {
        // Pas besoin de créer de nouveaux livres, nous supposons que les données de test existent déjà

        Emprunteur emprunteur = new Emprunteur("toto","toto@gmail.com","20","TotoVille");
        // Act
        emprunteurServiceImpl.save(emprunteur);
        // Act
        List<Emprunteur> emprunteurs = emprunteurRepository.findAll();

        // Assert
        assertEquals(1, emprunteurs.size()); // Supposons qu'il y ait deux livres de test dans la base de données
        // Effectuez d'autres assertions selon les données de test préexistantes

        // Affichage sur la console
        System.out.println("Liste des livres récupérée : " + emprunteurs);
    }

    @Test
    void testGetEmprunteurById() {
        // Arrange
        Emprunteur emprunteur = new Emprunteur("toto","toto@gmail.com","20","TotoVille");
        // Act
        emprunteurServiceImpl.save(emprunteur);
        Long existingEmprunteurId = 1L; // ID d'un livre existant en base de données

        // Act
        Emprunteur retrievedEmprunteur = emprunteurServiceImpl.getById(existingEmprunteurId);

        // Assert
        assertNotNull(retrievedEmprunteur);
        assertEquals(existingEmprunteurId, retrievedEmprunteur.getId());
        // Ajoutez d'autres assertions selon les attributs du livre attendus

        // Affichage sur la console
        System.out.println("Livre récupéré avec succès : " + retrievedEmprunteur);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Emprunteur emprunteur = new Emprunteur();
        emprunteur.setName("John Doe");
        emprunteur.setEmail("john.doe@example.com");
        emprunteur.setAge("30");
        emprunteur.setAdresse("123 Street");
        emprunteurRepository.save(emprunteur);

        // Act
        emprunteurRepository.deleteById(emprunteur.getId());

        // Assert
        assertFalse(emprunteurRepository.findById(emprunteur.getId()).isPresent());
    }
}
