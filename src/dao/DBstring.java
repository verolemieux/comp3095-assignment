package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBstring {
    private static String username = "admin";
	private static String password = "";
	private static String database = "COMP3095";
    private static Connection connect = null;

    public DBstring(){
    }
       
	public Connection connectDataBase() throws Exception {
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

}