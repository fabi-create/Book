package sn.esmt.mpisi2.controller;


import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
public class RegistrationControllerApi {

    private DefaultUserService userService;

    public RegistrationControllerApi(DefaultUserService userService) {
        super();
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUserAccount(@RequestBody UserRegisteredDTO registrationDto) {
        userService.save(registrationDto);
        return ResponseEntity.ok("User registered successfully");
    }
}
