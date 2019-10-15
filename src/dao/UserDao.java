package dao;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
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
			Class.forName("com.mysql.jdbc.Driver");
			//connect to DB and return connection
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + database + "?" + "user=" + username + "&password=" + password);
			return connect;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * don't think this is used at all
	 * public void readDataBase() throws Exception {
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
	}*/

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
				if(validatePassword(password, resultSet.getString(2)))/*(resultSet.getString(2).equals(password))*/
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
		User authUser = new User(resultSet.getString(1).toString(), resultSet.getString(2).toString(), resultSet.getString(3).toString(), resultSet.getString(4).toString(), resultSet.getString(5).toString(), Integer.parseInt(resultSet.getString(6).toString()), resultSet.getString(7).toString(), resultSet.getString(8).toString());
		// debug code
		System.out.println(resultSet.getString(1).toString() + " " + resultSet.getString(2).toString() + " " + resultSet.getString(3).toString() + " " + resultSet.getString(4).toString() + " " + resultSet.getString(5).toString() + " " + Integer.parseInt(resultSet.getString(6).toString()) + " " + resultSet.getString(7).toString() + " " + resultSet.getString(8).toString());
		return authUser;	
	}
	public boolean insertDB(String firstname, String lastname, String address, String email, String role, String password)
			throws Exception {
		boolean success = false;
		try {
			String hashedPassword = generatePassword(password);
			
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
			preparedStmt.setString(8, "abc123");
			preparedStmt.setInt(9, 0);
			preparedStmt.setString(10, hashedPassword);

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

	public String generatePassword(String password) throws NoSuchAlgorithmException, InvalidKeyException {
		String generatePasswordhash = "";
		byte[] hash;
		int iterations = 1000; // How many iteration of the algorithm would take to guess the hashed password
		char[] chars = password.toCharArray(); // Transforms password to sequence of characters
		byte[] salt = getSalt();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8); // Password-Based-Key-Derivative-Function
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // Uses PBKD algorithm
		try {
			hash = skf.generateSecret(spec).getEncoded();
			generatePasswordhash = iterations + ":" +toHex(salt) + ":" + toHex(hash); //It adds the iterations and the salt together
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatePasswordhash;
	}
	private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); 
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
	}
	private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
	}


	private boolean validatePassword(String password, String hashed) throws NoSuchAlgorithmException, InvalidKeySpecException{
		String[] parts = hashed.split(":"); //It devides the hashed password into the salt and the hash
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
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
