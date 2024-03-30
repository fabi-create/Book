package sn.esmt.mpisi2.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.esmt.mpisi2.DTO.UserDTO;
import sn.esmt.mpisi2.DTO.UserWithRoleDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/adminScreen")
public class AdminControllerApi {


    @Autowired
    private DefaultUserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    // Autres méthodes du contrôleur restent inchangées


    @GetMapping("/withRoles")
    public ResponseEntity<List<UserDTO>> getUsersWithRoles() {
        List<UserDTO> usersWithRoles = userService.getUsersWithRole();
        return new ResponseEntity<>(usersWithRoles, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        try {
            userDTO.setId(id); // Mettre à jour l'ID de l'utilisateur avec celui fourni dans le chemin
            userService.updateUserApi(userDTO);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> addRoleToUser(@PathVariable int userId, @PathVariable int roleId) {
        userService.addRoleToUserApi(userId, roleId);
        return ResponseEntity.ok("Role added to user successfully");
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> removeRoleFromUser(@PathVariable int userId, @PathVariable int roleId) {
        userService.removeRoleFromUserApi(userId, roleId);
        return ResponseEntity.ok("Role removed from user successfully");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }
    }
}
