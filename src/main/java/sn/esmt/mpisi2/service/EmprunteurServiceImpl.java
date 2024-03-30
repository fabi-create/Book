package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.DTO.EmprunteurDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.exception.MissingFieldException;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmprunteurServiceImpl implements EmprunteurService {

//    @Autowired
//    private EmprunteurRepository emprunteurRepository;

    private final EmprunteurRepository emprunteurRepository;
    @Autowired
    public EmprunteurServiceImpl(EmprunteurRepository emprunteurRepository) {
        this.emprunteurRepository=emprunteurRepository;
    }




    @Override
    public List<Emprunteur> getAllEmprunteur() {
        return emprunteurRepository.findAll();
    }

    @Override
    public void save(Emprunteur emprunteur) {
        if(Objects.nonNull(emprunteur)){
            emprunteurRepository.save(emprunteur);
        }
    }

    @Override
    public Emprunteur getById(Long id) {
        Emprunteur emprunteur = null;
        if (Objects.nonNull(id)) {
            Optional<Emprunteur> optionalEmprunteur = emprunteurRepository.findById(id);
            if(optionalEmprunteur.isPresent()){
                emprunteur = optionalEmprunteur.get();
            }else{
                throw new RuntimeException("Emprunteur not found with the id:"+ id);
            }
        }
        return emprunteur;
    }

    @Override
    public void deleteById(Long id) {
        if(Objects.nonNull(id)){
            emprunteurRepository.deleteById(id);
        }
    }


    @Override
    public List<EmprunteurDTO> getAllEmprunteurDTO() {
        List<Emprunteur> emprunteurs = emprunteurRepository.findAll();
        List<EmprunteurDTO> emprunteursDTO = convertToDTO(emprunteurs);
        return emprunteursDTO;
    }

    private List<EmprunteurDTO> convertToDTO(List<Emprunteur> emprunteurs) {
        List<EmprunteurDTO> emprunteursDTO = new ArrayList<>();
        for (Emprunteur emprunteur : emprunteurs) {
            emprunteursDTO.add(convertToDTO(emprunteur));
        }
        return emprunteursDTO;
    }

    private EmprunteurDTO convertToDTO(Emprunteur emprunteur) {
        EmprunteurDTO emprunteurDTO = new EmprunteurDTO();
        emprunteurDTO.setId(emprunteur.getId());
        emprunteurDTO.setName(emprunteur.getName());
        emprunteurDTO.setEmail(emprunteur.getEmail());
        emprunteurDTO.setAge(emprunteur.getAge());
        emprunteurDTO.setAdresse(emprunteur.getAdresse());
        return emprunteurDTO;
    }
}