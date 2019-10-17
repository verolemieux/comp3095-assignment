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
	
	
	public boolean insertDB(User insertUser, Role insertRole)
			throws Exception {
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
			resultSet = statement.executeQuery(String.format("SELECT count(role) FROM userrole WHERE userid ='%s'", userId));
			resultSet.next();
			int numRows = resultSet.getInt(1);
			Role[] DBRoles = new Role[numRows];
			resultSet = statement.executeQuery(String.format("SELECT role FROM userrole WHERE userid ='%s'", userId));
			int index = 0;
			while(resultSet.next())
			{
				ResultSet resultsRoles = statement.executeQuery(String.format("SELECT role FROM roles WHERE roleid='%s'", resultSet.getString(1)));
				resultsRoles.next();
				Role ArrayRole = new Role();
				ArrayRole.setId(Integer.parseInt(resultSet.getString(1)));
				ArrayRole.setRole(resultsRoles.getString(1));
				DBRoles[index] = ArrayRole;
			}
			
			
		} finally {
			connect.close();
		}
		return null;
	}

}
