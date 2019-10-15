package beans;

public class User {
	private String firstname;
	private String lastname;
	private String address;
	private String email;
	private String password;
	private String verificationkey;
	private boolean verified;
	private String role;

	public User(String firstname, String lastname, String address, String email, String verificationkey, boolean verified, String password, String role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.email = email;
		this.password = password;
		this.verificationkey = verificationkey;
		this.verified = verified;
		this.role = role;
	}	

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVerificationkey() {
		return verificationkey;
	}

	public void setVerificationkey(String verificationkey) {
		this.verificationkey = verificationkey;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
