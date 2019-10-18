/*********************************************************************************
* Project: < ABC Financial Institution >
* Assignment: < 1>
* Author(s): < Jeremy Thibeau, Veronyque Lemieux, Sergio Lombana, Ian Miranda>
* Student Number: < 101157911, 101106553, 101137768, 101163981>
* Date: October 18, 2019
* Description: Implements the connection string used by our DAOs.
*********************************************************************************/

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