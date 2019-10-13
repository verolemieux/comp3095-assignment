package dao;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Console;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import email.Email;

public class UserDao {

	private static String username = "admin";
	private static String password = "";
	private static String database = "COMP3095";
	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public UserDao() {

	}

////////////INTERACT WITH DB METHODS//////////////////////////////////////////

	public static Connection connectDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + database + "?" + "user=" + username + "&password=" + password);
			return connect;
		} catch (Exception e) {
			throw e;
		}
	}

	public void readDataBase() throws Exception {
		try {
			connect = connectDataBase();
			// CREATE STATEMENT
			statement = connect.createStatement();
			// GET RESULT OF SQL QUERY
			resultSet = statement.executeQuery("SELECT * FROM users");
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = resultSet.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println("");
			}

		} finally {
			connect.close();
		}
	}

	public boolean userExists(String email) throws Exception {
		boolean exists = false;
		try {
			connect = connectDataBase();
			statement = connect.createStatement();
			//Jeremy's changes below - modified SQL statement
			resultSet = statement.executeQuery(String.format("SELECT email FROM users WHERE email ='%s'", email));
			if(resultSet.next())
			{
				exists = true;
				System.out.println("User exists");
			}
			//ResultSetMetaData rsmd = resultSet.getMetaData();
			/*int columnsNumber = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 0; i <= columnsNumber; i++) {
					//if (i > 1) {
						if (email == resultSet.getString(i)) {
							exists = true;
						//}
					}
				}
			}*/

		} finally {
			connect.close();
		}
		return exists;
	}

	public boolean validateUser(String email, String password) throws Exception
	{
		if(userExists(email))
		{
			try {
				connect = connectDataBase();
				statement = connect.createStatement();
				//Jeremy's changes below - modified SQL statement
				resultSet = statement.executeQuery(String.format("SELECT email, password FROM users WHERE email ='%s'", email));
				resultSet.next();
				if(resultSet.getString(2).equals(password))
				{
					return true;
				}
			} finally {
				connect.close();
			}
		}
		return false;
	}
	public boolean insertDB(String firstname, String lastname, String email, String role, String password)
			throws Exception {
		boolean success = false;
		try {
			if(userExists(email))
			{
				return false;
			}
			connect = connectDataBase();
			statement = connect.createStatement();
			String query = "INSERT INTO users (id, firstname, lastname, email, role, created, password)"
					+ "values(?,?,?,?,?,?,?)";
			Calendar calendar = Calendar.getInstance();
			Date startDate = new Date(calendar.getTime().getTime());
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			preparedStmt.setString(1, generateID());
			preparedStmt.setString(2, firstname);
			preparedStmt.setString(3, lastname);
			preparedStmt.setString(4, email);
			preparedStmt.setString(5, "client");
			preparedStmt.setDate(6, startDate);
			preparedStmt.setString(7, password);

			if (preparedStmt.execute()) {
				success = true;
			}

		} finally {
			connect.close();
		}
		return success;
	}

	public String generateID() throws Exception {
		int lastId = 0;
		try {
			String temp = "";
			connect = connectDataBase();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT Max(id) FROM users");
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i >= 1) {
						temp = resultSet.getString(i);
					}
				}
			}
			lastId = Integer.parseInt(temp);
			lastId += 1;
		} finally {
			connect.close();
		}
		return Integer.toString(lastId);
	}

////////////////////////////////////////////////////////////

	public boolean hasSpecial(String s) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if (b) {
			return true;
		}
		return false;
	}

	public boolean isEmpty(String s) {
		if (s == null || s.trim().isEmpty() || s.equals("")) {
			return true;
		}
		return false;
	}

	public boolean isEmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + // PATTERN FOR EMAIL VALIDATION
				"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches(); // RETURNS A BOOLEAN
	}

	public boolean isPasswordValid(String password) {
		boolean isLengthValid = true;
		if (password.length() < 6 || password.length() > 12) {
			isLengthValid = false;
		}
		boolean hasUppercase = !password.equals(password.toLowerCase());
		boolean hasLowercase = !password.equals(password.toUpperCase());
		boolean hasSpecialc = hasSpecial(password);
		if (isLengthValid && hasUppercase && hasLowercase && hasSpecialc) {
			return true;
		}
		return false;
	}

	public boolean usernameExists(String username) {
		/*
		 * if (username != null && username.length() > 0) { //check if username exists
		 * in database if (true) { return true; } return false; } return false;
		 */
		return true;
	}

	public void sendResetPasswordEmail(String username) {
		Email email = new Email();
		email.createResetPasswordMessageEmail(username, "Veronyque", "a1b2c3");
	}

	public boolean keyMatchesUser(String username, String key) {
		// check if key from URL matches username's key in database
		return true;
	}

	public void resetPassword(String username, String password) {
		// update database with new password
	}
}
