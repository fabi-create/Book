package sn.esmt.mpisi2.service;


import sn.esmt.mpisi2.DTO.UserDTO;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.model.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);


	//User update(Integer id, UserRegisteredDTO updatedUserData);

	User addRoleToUser(User user, Role role);



	User update(User userRegisteredDTO);

	//	User saveUserWithRoles(User user, Set<String> roleNames);
	Map<User, Set<Role>> getUsersWithRoles();



	// lister en mode api
	List<User> getAllUser();

	List<Role> getAllRole();

	List<UserDTO> getUsersWithRole();

 //void updateUserApi(UserDTO userDTO);

	void updateUserApi(UserDTO userDTO);

	void addRoleToUserApi(int userId, int roleId);

	void removeRoleFromUserApi(int userId, int roleId);

	User getUserById(Integer id);

	void deleteById(Integer id);

	User removeRoleFromUser(User user, Role role);
}
