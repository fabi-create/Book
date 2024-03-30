package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.DTO.ReservationDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements  ReservationService{

 @Autowired
    ReservationRepository reservationRepository;

 @Autowired
    EmprunteurRepository emprunteurRepository;



    @Override
    public void save(Reservation reservation) {
        if(Objects.nonNull(reservation)){
            reservationRepository.save(reservation);
        }
    }

 @Override
    public List<Emprunteur> getAllEmprunteurs() {
        return emprunteurRepository.findAll();
    }


    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }


    @Override
    public List<Reservation> getAllReservation(){
        return reservationRepository.findAll();
    }



    @Override
    public Reservation getReservationById(int id) {
        return reservationRepository.findById(id).get();
    }



    @Override
    public void deleteById(int id) {
        if(Objects.nonNull(id)){
            reservationRepository.deleteById(id);
        }
    }

    @Override
    public void updateReservation(int id, long emprunteurId, Reservation updatedReservation) {
        // Récupérer la réservation existante par son ID
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        Emprunteur emprunteur = emprunteurRepository.findById(emprunteurId).orElse(null);

        if (optionalReservation.isPresent() && emprunteur != null) {
            Reservation reservation = optionalReservation.get();

            // Mettre à jour les champs de la réservation existante avec les nouvelles valeurs
            reservation.setCustomId(updatedReservation.getCustomId());
            reservation.setAuthor(updatedReservation.getAuthor());
            reservation.setName(updatedReservation.getName());
            // Mettre à jour d'autres champs selon vos besoins

            // Mettre à jour l'emprunteur associé à la réservation
            reservation.setEmprunteur(emprunteur);

            // Sauvegarder la réservation mise à jour
            reservationRepository.save(reservation);
        } else {
            // Gérer le cas où aucune réservation avec l'ID spécifié n'est trouvée
            // ou aucun emprunteur avec l'ID spécifié n'est trouvé
            // Vous pouvez lancer une exception appropriée ou gérer de manière appropriée
        }
    }


    @Override
    @Transactional
    public void updateEmprunteurId(int id, long emprunteurId) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        Emprunteur emprunteur = emprunteurRepository.findById(emprunteurId).orElse(null); // Chargez l'entité Emprunteur à partir de la base de données
        if (reservation  != null && emprunteur != null) {
            reservation .setEmprunteur(emprunteur); // Associez l'emprunteur à l'entité MyBookList
            reservationRepository.save(reservation );
        } else {
            // Gérer le cas où aucun élément avec l'ID spécifié n'est trouvé
        }
    }


    @Override
    public List<ReservationDTO> getAllReservationDTO() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDTO> reservationDTOs = convertReservationToDTO(reservations);
        return reservationDTOs;
    }

    private List<ReservationDTO> convertReservationToDTO(List<Reservation> reservations) {
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(convertToDTO(reservation));
        }
        return reservationDTOs;
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setCustomId(reservation.getCustomId());
        reservationDTO.setName(reservation.getName());
        reservationDTO.setAuthor(reservation.getAuthor());
        reservationDTO.setNom_emprunteur(reservation.getNom_emprunteur());
        reservationDTO.setEmprunteur(reservation.getEmprunteur().getId());
        return reservationDTO;
    }




}
