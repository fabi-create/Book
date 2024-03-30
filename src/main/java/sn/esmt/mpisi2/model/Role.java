package sn.esmt.mpisi2.model;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String role;

    public Role() {
    }

	public Role(String role) {
		this.role = role; // Initialise la propriété role
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@ManyToMany(mappedBy = "roles")
	private Set<User> users;

	// Constructors, getters, setters, etc.

	public Set<User> getUsers() {
		return users;
	}
}
