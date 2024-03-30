package sn.esmt.mpisi2.controller;

import sn.esmt.mpisi2.DTO.UserLoginDTO;
import sn.esmt.mpisi2.DTO.UserWithRoleDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.*;
import sn.esmt.mpisi2.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private DefaultUserService userService;

	@ModelAttribute("user")
	public UserLoginDTO userLoginDTO() {
		return new UserLoginDTO();
	}

	@GetMapping
	public String login() {
		return "login";
	}

	@PostMapping
	public void  loginUser(@ModelAttribute("user")
						   UserLoginDTO userLoginDTO) {
		userService.loadUserByUsername(userLoginDTO.getUsername());
	}
}
