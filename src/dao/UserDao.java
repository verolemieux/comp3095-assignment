package dao;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import dao.DBstring;
import beans.User;
import email.Email;

public class UserDao {

	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private DBstring dbConnect = new DBstring();

	public UserDao() {

	}

/////////// INTERACT WITH DATABASE ///////////

	public boolean userExists(String email) throws Exception {
		boolean exists = false;
		try {
			connect = dbConnect.connectDataBase();
			statement = connect.createStatement();
			resultSet = statement.executeQuery(String.format("SELECT email FROM users WHERE email ='%s'", email));
			//if result set has a value in it, will have found the user - if not return false
			if(resultSet.next())
			{
				exists = true;
			}
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
				connect = dbConnect.connectDataBase();
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
		connect = dbConnect.connectDataBase();
		statement = connect.createStatement();
		resultSet = statement.executeQuery(String.format("SELECT firstname, lastname, address, email, password, verified, verificationkey FROM users WHERE email ='%s'", email));
		resultSet.next();
		User authUser = new User();
		authUser.setFirstname(resultSet.getString(1).toString());
		authUser.setLastname(resultSet.getString(2).toString());
		authUser.setAddress(resultSet.getString(3).toString());
		authUser.setEmail(resultSet.getString(4).toString());
		authUser.setVerificationkey(resultSet.getString(7).toString());
		authUser.setVerified(Integer.parseInt(resultSet.getString(6).toString()));
		authUser.setPassword(resultSet.getString(5).toString());
		return authUser;	
	}

	public boolean insertDB(String firstname, String lastname, String address, String email, String verificationKey, String password)
			throws Exception {
		boolean success = false;
		try {
			String hashedPassword = generatePassword(password);
			connect = dbConnect.connectDataBase();
			statement = connect.createStatement();
			System.out.println("testing");
			String query = "INSERT INTO users (userid, firstname, lastname, address, email, created, verificationkey, verified, password)"
					+ "values(?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			preparedStmt.setString(1, generateID());
			preparedStmt.setString(2, firstname);
			preparedStmt.setString(3, lastname);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, email);
			preparedStmt.setTimestamp(6,java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			preparedStmt.setString(7, verificationKey);
			preparedStmt.setInt(8, 0);
			preparedStmt.setString(9, hashedPassword);

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
			connect = dbConnect.connectDataBase();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT Max(userid) FROM users");
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

/////////// HASH PASSWORD ///////////

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
	
/////////// VALIDATE USER INPUT ///////////

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
				connect = dbConnect.connectDataBase();
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
		email.createRegistrationMessageEmail(user);
	}
	
	public boolean verifyEmail(User user, String key) throws Exception {
		boolean success = false;
		try {			
			connect = dbConnect.connectDataBase();
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
		email.createResetPasswordMessageEmail(user);
	}

	public boolean resetPassword(String username, String password) throws Exception {
		boolean success = false;
		try {
			connect = dbConnect.connectDataBase();
			statement = connect.createStatement();
			String hashedPassword = generatePassword(password);
			String query = String.format("UPDATE users set password='%s' where email='%s'", hashedPassword, username);
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
