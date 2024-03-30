package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.DTO.EmprunteurDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;

import java.util.List;

public interface EmprunteurService {
    List<Emprunteur> getAllEmprunteur();
    void save(Emprunteur emprunteur);
    Emprunteur getById(Long id);
    void deleteById(Long id);

    List<EmprunteurDTO> getAllEmprunteurDTO();
}
