package sn.esmt.mpisi2.controller;


import org.springframework.web.multipart.MultipartFile;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
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
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/adminScreen")
public class AdminController {



	@Autowired
	private DefaultUserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;



	@GetMapping
	public String displayDashboard(Model model){
		String user= returnUsername();
		model.addAttribute("userDetails", user);
		return "adminScreen";
	}



	private String returnUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
		User users = userRepository.findByEmail(user.getUsername());
		return users.getName();
	}




	@PostMapping("/update")
	public String updateUser(@ModelAttribute User userRegisteredDTO) {


		userService.update(userRegisteredDTO);
		return "redirect:/adminScreen/users"; // Redirigez l'utilisateur vers une page appropriée après la mise à jour
	}





	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable("id") int id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return "edit-user";
	}


	@GetMapping("/users")
	public String showUsersWithRoles(Model model) {
		Map<User, Set<Role>> usersWithRoles = userService.getUsersWithRoles();
		model.addAttribute("users", usersWithRoles.keySet());
		model.addAttribute("roles", usersWithRoles.values());
		return "users-list"; // Assurez-vous d'avoir une vue correspondante avec ce nom
	}

	@GetMapping("/addRoleToUser")
	public String showAddRolePage(Model model) {
		List<User> users = userRepository.findAll();
		List<Role> roles = roleRepository.findAll();

		model.addAttribute("users", users);
		model.addAttribute("roles", roles);
		model.addAttribute("userWithRole", new UserWithRoleDTO()); // DTO pour la saisie du formulaire

		return "addRoleToUser";
	}

	@GetMapping("/removeRoleToUser")
	public String showRemoveRolePage(Model model) {
		List<User> users = userRepository.findAll();
		List<Role> roles = roleRepository.findAll();

		model.addAttribute("users", users);
		model.addAttribute("roles", roles);
		model.addAttribute("userWithRole", new UserWithRoleDTO()); // DTO pour la saisie du formulaire

		return "removeRoleToUser";
	}

	@PostMapping("/addRoleToUser")
	public String addRoleToUser(@ModelAttribute("userWithRole") UserWithRoleDTO userWithRole) {
		Integer userId = userWithRole.getUserId();
		Integer roleId = userWithRole.getRoleId();

		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

		userService.addRoleToUser(user, role);

		return "redirect:/adminScreen/users";
	}

	@PostMapping("/removeRoleToUser")
	public String removeRoleToUser(@ModelAttribute("userWithRole") UserWithRoleDTO userWithRole) {
		Integer userId = userWithRole.getUserId();
		Integer roleId = userWithRole.getRoleId();

		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

		//userService.addRoleToUser(user, role);
		userService.removeRoleFromUser(user, role);

		return "redirect:/adminScreen/users";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable Integer id) {
		userService.deleteById(id);
		return "redirect:/adminScreen/users";
	}
}
