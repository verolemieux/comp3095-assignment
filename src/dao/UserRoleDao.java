package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import beans.Role;
import beans.User;
import dao.DBstring;

public class UserRoleDao {


	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private DBstring dbConnect = new DBstring();
	
	
	public boolean insertDB(User insertUser, Role insertRole) throws Exception {
		//inserts User + Role combo into DB
		boolean success = false;
		try {
			
			connect = dbConnect.connectDataBase();
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
			
			connect = dbConnect.connectDataBase();
			statement = connect.createStatement();
			
			//find out how many roles a user has
			resultSet = statement.executeQuery(String.format("SELECT count(*) FROM userrole WHERE userid ='%s'", userId));
			resultSet.next();
			int numRows = resultSet.getInt(1);
			
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
			
			//put all roles that match the User ID into an array and return that array, this will never return 0
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
				DBRoles[index] = ArrayRole;
				index++;
			}
			return DBRoles;
			
			
		} finally {
			connect.close();
		}
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
