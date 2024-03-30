package sn.esmt.mpisi2.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.esmt.mpisi2.model.*;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);


}
