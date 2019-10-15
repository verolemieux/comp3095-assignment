package dao;

import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.User;

import java.io.Console;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
			Class.forName("com.mysql.jdbc.Driver");
			//connect to DB and return connection
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3307/" + database + "?" + "user=" + username + "&password=" + password);
			return connect;
		} catch (Exception e) {
			throw e;
		}
	}

	public void readDataBase() throws Exception {
		try {
			//reads full database, is this function even needed?
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
			resultSet = statement.executeQuery(String.format("SELECT email FROM users WHERE email ='%s'", email));
			//if result set has a value in it, will have found the user - if not return false
			if(resultSet.next())
			{
				exists = true;
			}
			//below isn't used kept for reference
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
		//check if user exists
		if(userExists(email))
		{
			try {
				//if user exists, check to ensure password is valid
				connect = connectDataBase();
				statement = connect.createStatement();
				resultSet = statement.executeQuery(String.format("SELECT email, password FROM users WHERE email ='%s'", email));
				resultSet.next();
				if(resultSet.getString(2).equals(password))
				{
					getUser(email);
					return true;
				}
			} finally {
				connect.close();
			}
		}
		return false;
	}
	public User getUser(String email) throws Exception
	{
		//pulls a User object from the database
		connect = connectDataBase();
		statement = connect.createStatement();
		resultSet = statement.executeQuery(String.format("SELECT firstname, lastname, address, email, password, verified, verificationkey, role FROM users WHERE email ='%s'", email));
		resultSet.next();
		User authUser = new User(resultSet.getString(1).toString(), resultSet.getString(2).toString(), resultSet.getString(3).toString(), resultSet.getString(4).toString(), resultSet.getString(7).toString(), Integer.parseInt(resultSet.getString(6).toString()), resultSet.getString(5).toString(), resultSet.getString(8).toString());
		// debug code
		System.out.println(resultSet.getString(1).toString() + " " + resultSet.getString(2).toString() + " " + resultSet.getString(3).toString() + " " + resultSet.getString(4).toString() + " " + resultSet.getString(5).toString() + " " + Boolean.parseBoolean(resultSet.getString(6).toString()) + " " + resultSet.getString(7).toString() + " " + resultSet.getString(8).toString());
		return authUser;
	}
	public boolean insertDB(String firstname, String lastname, String address, String email, String role, String verificationKey, String password)
			throws Exception {
		boolean success = false;
		try {
			connect = connectDataBase();
			statement = connect.createStatement();
			String query = "INSERT INTO users (id, firstname, lastname, address, email, role, created, verificationkey, verified, password)"
					+ "values(?,?,?,?,?,?,?,?,?,?)";
			Calendar calendar = Calendar.getInstance();
			Date startDate = new Date(calendar.getTime().getTime());
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			preparedStmt.setString(1, generateID());
			preparedStmt.setString(2, firstname);
			preparedStmt.setString(3, lastname);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, email);
			preparedStmt.setString(6, "client");
			preparedStmt.setDate(7, startDate);
			preparedStmt.setString(8, verificationKey);
			preparedStmt.setInt(9, 0);
			preparedStmt.setString(10, password);

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
	
	public boolean keyMatchesUser(String username, String key) throws Exception {
		if(userExists(username))
		{
			try {
				connect = connectDataBase();
				statement = connect.createStatement();
				resultSet = statement.executeQuery(String.format("SELECT verificationkey FROM users WHERE email ='%s'", username));
				resultSet.next();
				if(resultSet.getString(1).equals(key))
				{
					return true;
				}
			} finally {
				connect.close();
			}
		}
		return false;
	}

/////////// VERIFY EMAIL ///////////
	
	public void sendEmailVerificationEmail(User user) {
		Email email = new Email();
		email.createRegistrationMessageEmail(user.getEmail(), user.getFirstname(), user.getLastname(), user.getVerificationkey());
	}
	
	public boolean verifyEmail(User user, String key) throws Exception {
		boolean success = false;
		try {			
			connect = connectDataBase();
			statement = connect.createStatement();
			String query = String.format("UPDATE users set verified=1 where email='%s'", user.getEmail());
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			if (preparedStmt.execute()) {
				success = true;
			}
		} finally {
			connect.close();
		}
		return success;
	}
	
/////////// RESET PASSWORD ///////////

	public void sendResetPasswordEmail(User user) {
		Email email = new Email();
		email.createResetPasswordMessageEmail(user.getEmail(), user.getFirstname(), user.getVerificationkey());
	}

	public boolean resetPassword(String username, String password) throws Exception {
		boolean success = false;
		try {
			connect = connectDataBase();
			statement = connect.createStatement();
			String query = String.format("UPDATE users set password='%s' where email='%s'", password, username);
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			if (preparedStmt.execute()) {
				success = true;
			}
		} finally {
			connect.close();
		}
		return success;
	}
}
