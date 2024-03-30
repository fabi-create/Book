package sn.esmt.mpisi2.service;

import sn.esmt.mpisi2.DTO.ReservationDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;

import javax.transaction.Transactional;
import java.util.List;

public interface ReservationService {


    void save(Reservation reservation);

    List<Emprunteur> getAllEmprunteurs();

    void saveReservation(Reservation reservation);

    List<Reservation> getAllReservation();

    Reservation getReservationById(int id);

    void deleteById(int id);

    void updateReservation(int id, long emprunteurId, Reservation updatedReservation);


    @Transactional
    void updateEmprunteurId(int id, long emprunteurId);

    List<ReservationDTO> getAllReservationDTO();
}
