package sn.esmt.mpisi2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.esmt.mpisi2.model.*;

@Repository
public interface EmprunteurRepository  extends JpaRepository<Emprunteur, Long> {
}

