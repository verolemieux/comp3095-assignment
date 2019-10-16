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
					"jdbc:mysql://localhost:3307/" + database + "?" + "user=" + username + "&password=" + password);
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
					+ "values(?,?,?,?,?,?,?,?,?,?)";
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
			resultSet = statement.executeQuery(String.format("SELECT count(role) FROM userrole WHERE userid ='%s'", userId));
			resultSet.next();
			int numRows = resultSet.getInt(1);
			Role[] DBRoles = new Role[numRows];
			resultSet = statement.executeQuery(String.format("SELECT role FROM userrole WHERE userid ='%s'", userId));
			while(resultSet.next())
			{
				
			}
			
			
		} finally {
			connect.close();
		}
		return null;
	}

}
