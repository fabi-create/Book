package sn.esmt.mpisi2.DTO;


public class UserRegisteredDTO {


	private  int id;
    private String name;
	
	private String email_id;
	
	private String password;
	
	private String role;
	
	

	public UserRegisteredDTO() {
		super();
	}

	public UserRegisteredDTO(String role) {
		super();
		this.role = role;
	}



	public UserRegisteredDTO(String email_id, String password, String name, String role) {
		this.email_id = email_id;
		this.password = password;
		this.name = name;
		this.role = role;
	}
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getPassword() {
		return password;
	}

	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Integer getId() {
		return id;
	}
}
