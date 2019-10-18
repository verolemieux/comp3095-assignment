/*********************************************************************************
* Project: < ABC Financial Institution >
* Assignment: < 1>
* Author(s): < Jeremy Thibeau, Veronyque Lemieux, Sergio Lombana, Ian Miranda>
* Student Number: < 101157911, 101106554, 101137768, 101163981>
* Date: October 18, 2019
* Description: Contains definition for the Role class in the Roles table of the database.
*********************************************************************************/

package beans;

public class Role {
	private String role;
	int id;
	
	public Role()
	{
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
