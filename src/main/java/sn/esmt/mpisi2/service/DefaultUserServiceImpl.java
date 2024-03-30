package sn.esmt.mpisi2.service;

import sn.esmt.mpisi2.DTO.RoleDTO;
import sn.esmt.mpisi2.DTO.UserDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.model.*;
import sn.esmt.mpisi2.repository.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;

	@PersistenceContext
	private EntityManager entityManager;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
		Role role = new Role();
		if(userRegisteredDTO.getRole().equals("USER"))
			role = roleRepo.findByRole("USER");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
			role = roleRepo.findByRole("ADMIN");
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail_id());
		user.setName(userRegisteredDTO.getName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRole(role);

		return userRepo.save(user);
	}


	@Override
	public User update(User userRegisteredDTO) {
		// Récupérez l'utilisateur existant à partir de la base de données en utilisant son identifiant
		User existingUser = userRepo.findById(userRegisteredDTO.getId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Mettez à jour les champs de cet utilisateur avec les nouvelles informations fournies dans le DTO
		existingUser.setName(userRegisteredDTO.getName());
		existingUser.setEmail(userRegisteredDTO.getEmail());
		existingUser.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));

		// Enregistrez les modifications dans la base de données
		return userRepo.save(existingUser);
	}



	@Override
	public Map<User, Set<Role>> getUsersWithRoles() {
		List<User> users = userRepo.findAll();
		Map<User, Set<Role>> usersWithRoles = new HashMap<>();

		for (User user : users) {
			usersWithRoles.put(user, user.getRole());
		}

		return usersWithRoles;
	}


	// lister en mode api
	@Override
	public List<User> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public List<Role> getAllRole() {
		return roleRepo.findAll();
	}


//apiRest
	@Override
	public List<UserDTO> getUsersWithRole() {
		List<User> users = userRepo.findAll();
		List<Role> roles = roleRepo.findAll();

		Map<Integer, Set<Role>> userRolesMap = new HashMap<>();
		for (Role role : roles) {
			for (User user : role.getUsers()) {
				if (!userRolesMap.containsKey(user.getId())) {
					userRolesMap.put(user.getId(), new HashSet<>());
				}
				userRolesMap.get(user.getId()).add(role);
			}
		}

		List<UserDTO> usersWithRoles = new ArrayList<>();
		for (User user : users) {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(user.getId());
			userDTO.setName(user.getName());
			userDTO.setEmail(user.getEmail());
			userDTO.setPassword(user.getPassword());

			if (userRolesMap.containsKey(user.getId())) {
				Set<Role> userRoles = userRolesMap.get(user.getId());
				Set<RoleDTO> userRolesDTO = new HashSet<>();
				for (Role role : userRoles) {
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setId(role.getId());
					roleDTO.setRole(role.getRole());
					userRolesDTO.add(roleDTO);
				}
				userDTO.setRoles(userRolesDTO);
			}

			usersWithRoles.add(userDTO);
		}

		return usersWithRoles;
	}


	@Override
	public void updateUserApi(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId())
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userDTO.getId()));
		// Mettre à jour les informations de l'utilisateur avec les données de userDTO
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		// Enregistrer les modifications dans la base de données
		userRepo.save(user);
	}

	@Override
	public void addRoleToUserApi(int userId, int roleId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		Role role = roleRepo.findById(roleId)
				.orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
		// Ajouter le rôle à l'utilisateur s'il n'est pas déjà présent
		if (!user.getRole().contains(role)) {
			user.getRole().add(role);
			userRepo.save(user);
		}
	}

	@Override
	public void removeRoleFromUserApi(int userId, int roleId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		Role role = roleRepo.findById(roleId)
				.orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
		// Supprimer le rôle de l'utilisateur s'il est présent
		if (user.getRole().contains(role)) {
			user.getRole().remove(role);
			userRepo.save(user);
		}
	}

//apiRest
	@Override
	public User getUserById(Integer id) {
		return userRepo.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
	}



	@Override
	public void deleteById(Integer id) {
		User user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

		// Supprimer l'utilisateur
		userRepo.delete(user);
	}


	@Transactional
	@Override
	public User addRoleToUser(User user, Role role) {
		// Associer le rôle à l'utilisateur
		user.getRole().add(role);
		// Sauvegarder l'utilisateur pour appliquer les modifications
		return userRepo.save(user);
	}




	@Transactional
	@Override
	public User removeRoleFromUser(User user, Role role) {
		// Retirer le rôle de l'utilisateur
		user.getRole().remove(role);
		// Sauvegarder l'utilisateur pour appliquer les modifications
		return userRepo.save(user);
	}

}
