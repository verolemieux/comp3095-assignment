package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import dao.DBstring;

import beans.Role;
public class RoleDao {

	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private DBstring dbConnect = new DBstring();
	
	public RoleDao()
	{
	}

	public Role getRole(int id) throws Exception {
		try {
			connect = dbConnect.connectDataBase();
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
