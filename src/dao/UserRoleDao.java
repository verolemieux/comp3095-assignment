package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import beans.Role;
import beans.User;

public class UserRoleDao {

	private static String username = "admin";
	private static String password = "";
	private static String database = "COMP3095";
	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	
	public static Connection connectDataBase() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//connect to DB and return connection
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + database + "?" + "user=" + username + "&password=" + password);
			return connect;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean insertDB(User insertUser, Role insertRole)
			throws Exception {
		boolean success = false;
		try {
			
			connect = connectDataBase();
			statement = connect.createStatement();
			String query = "INSERT INTO userrole (userid, roleid)"
					+ "values(?,?)";
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			preparedStmt.setInt(1, insertUser.getId());
			preparedStmt.setInt(2, insertRole.getId());

			if (preparedStmt.execute()) {
				success = true;
			}

		} finally {
			connect.close();
		}
		return success;
	}
	public Role[] getRoles(int userId) throws Exception
	{
		try {
			
			connect = connectDataBase();
			statement = connect.createStatement();
			
			//find out how many roles a user has
			resultSet = statement.executeQuery(String.format("SELECT count(*) FROM userrole WHERE userid ='%s'", userId));
			resultSet.next();
			int numRows = resultSet.getInt(1);
			//debug code
			System.out.println("Number of roles by this user: " + numRows);
			
			//count how many roles are in database
			ResultSet resultSetallRoles = statement.executeQuery(String.format("SELECT count(*) FROM roles"));
			resultSetallRoles.next();
			int numRolesRows = resultSetallRoles.getInt(1);
			Role[] allRoles = new Role[numRolesRows];
			
			//get all roles out of database and put into an array
			int indexAllRoles = 0;
			ResultSet resultsRoles = statement.executeQuery(String.format("SELECT roleid, role FROM roles"));
			while(resultsRoles.next())
			{
				Role addRole = new Role();
				addRole.setId(resultsRoles.getInt(1));
				addRole.setRole(resultsRoles.getString(2));
				allRoles[indexAllRoles] = addRole;
				indexAllRoles++;
			}
			
			//
			Role[] DBRoles = new Role[numRows];
			ResultSet resultSet2 = statement.executeQuery(String.format("SELECT roleid FROM userrole WHERE userid ='%s'", userId));
			int index = 0;
			while(resultSet2.next())
			{
				//find out which index a role is at with the ID present in the current row
				int indexSearch = findIndex(allRoles, resultSet2.getInt(1));
				Role ArrayRole = new Role();
				//fill the role ID from this row, which corresponds to the index searched
				ArrayRole.setId(Integer.parseInt(resultSet2.getString(1)));
				ArrayRole.setRole(allRoles[indexSearch].getRole());
				//debug code
				System.out.println("RoleID: " + ArrayRole.getId() + " Role name: " + ArrayRole.getRole());
				DBRoles[index] = ArrayRole;
				index++;
			}
			return DBRoles;
			
			
		} finally {
			connect.close();
		}
		//return null;
	}
	public int findIndex(Role[] role, int roleid)
	{
		//find what index a roleID is at in the array of roles
		for(int i =0; i < role.length; i++)
		{
			if(role[i].getId() == roleid)
			{
				return i;
			}
		}
		return -1;
	}

}
