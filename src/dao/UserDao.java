package dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class UserDao {
	
	public UserDao() {
		// empty instantiation
		// so that we may call methods built into UserDao class
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
