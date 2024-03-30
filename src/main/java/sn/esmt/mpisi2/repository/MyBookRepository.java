package sn.esmt.mpisi2.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.esmt.mpisi2.model.*;

import java.util.List;

@Repository
public interface MyBookRepository extends JpaRepository<MyBookList,Integer>{

    List<MyBookList> findByCustomId(int custom_id);
}
