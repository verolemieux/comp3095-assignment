package dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;


public class UserDao {
	
	private static String username = "admin";
	private static String password = "";
	private static String database = "COMP3095";
	private static Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public UserDao() {
		
	}
	  
	public static Connection connectDataBase() throws Exception {
	    try {
	      // This will load the MySQL driver, each DB has its own driver
	      Class.forName("com.mysql.jdbc.Driver");
	      // Setup the connection with the DB
	      connect = DriverManager
		          .getConnection("jdbc:mysql://localhost:3307/"+database+"?"
		              + "user="+username+"&password="+password);
	      return connect;
	    } catch (Exception e) {
	      throw e;
	    } 
	  }
	
	public void readDataBase() throws Exception {
		try {
			//THIS WILL LOAD THE MYSQL DRIVER
			Class.forName("com.mysql.jdbc.Driver");
		    // Setup the connection with the DB
		    connect = DriverManager
			         .getConnection("jdbc:mysql://localhost:3307/"+database+"?"
			              + "user="+username+"&password="+password);
		    // CREATE STATEMENT
		    statement = connect.createStatement();
		    // GET RESULT OF SQL QUERY
		    resultSet = statement.executeQuery("SELECT * FROM users");
		    ResultSetMetaData rsmd = resultSet.getMetaData();
		    int columnsNumber = rsmd.getColumnCount();
		    while (resultSet.next()) {
		        for (int i = 1; i <= columnsNumber; i++) {
		            if (i > 1) System.out.print(",  ");
		            String columnValue = resultSet.getString(i);
		            System.out.print(columnValue + " " + rsmd.getColumnName(i));
		        }
		        System.out.println("");
		    }		    
			
		}finally {
		 connect.close();
	    }
	}
	
	public boolean hasSpecial(String s) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if(b) {
			return true;
		}
		return false;
	}
	public boolean isEmpty(String s) {
		if(s == null||s.trim().isEmpty()||s.equals("")) {
			return true;
		}
		return false;
	}
	public boolean isEmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ //PATTERN FOR EMAIL VALIDATION
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); //RETURNS A BOOLEAN
	}
	public boolean isPasswordValid(String password) {
		boolean isLengthValid = true;
		if(password.length()<6||password.length()>12) {
			isLengthValid = false;
		}
		boolean hasUppercase = !password.equals(password.toLowerCase());
		boolean hasLowercase = !password.equals(password.toUpperCase());
		boolean hasSpecialc = hasSpecial(password);
		if(isLengthValid&&hasUppercase&&hasLowercase&&hasSpecialc) {
			return true;
		}
		return false;

	}
	
}
