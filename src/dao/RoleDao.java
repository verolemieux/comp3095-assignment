package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import beans.Role;
public class RoleDao {

	private static String username = "admin";
	private static String password = "";
	private static String database = "COMP3095";
	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public RoleDao()
	{
	}
	
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
	
	public Role getRole(int id) throws Exception {
		try {
			connect = connectDataBase();
			statement = connect.createStatement();
			resultSet = statement.executeQuery(String.format("SELECT role FROM roles WHERE roleid ='%s'", id));
			if(resultSet.next())
			{
				Role outputRole = new Role();
				outputRole.setRole(resultSet.getString(1));
				return outputRole;
			}
			
		} catch (Exception e){
			throw e;
		}
		return null;
	}
	
}
