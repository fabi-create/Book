package sn.esmt.mpisi2.controller;


import sn.esmt.mpisi2.DTO.UserLoginDTO;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginControllerApi {

    @Autowired
    private DefaultUserService userService;

    @ModelAttribute("user")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }

    @PostMapping
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            // Authentification de l'utilisateur
            userService.loadUserByUsername(userLoginDTO.getUsername());
            // Si l'authentification réussit, renvoie une réponse OK
            return ResponseEntity.ok("Utilisateur authentifié avec succès.");
        } catch (UsernameNotFoundException e) {
            // Si l'authentification échoue, renvoie une réponse d'erreur avec le message approprié
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de l'authentification : nom d'utilisateur ou mot de passe incorrect.");
        }
    }



}
