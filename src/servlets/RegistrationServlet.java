package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/Register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RegistrationServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("confirm_password");
		String[] params = new String[] {firstname,lastname,address,email,password,passwordConfirm};
		String[] var_names = new String[] {"First name","Last name","Address","Email","Password","Confirm Password"}; //MADE AN ARRAY TO VALIDATE FASTER
		String message = "";
		String color = "";
		boolean isValid = true;
		//SHOULD I DO AUTHENTICATION IN THIS METHOD?
		//USER.java CLASS IS FOR WHEN A USER LOGS IN
		//I SHOULD DO VALIDATION HERE AND DIRECLTY STORE IN THE DATABASE
		for(int i = 0; i<params.length;i++) {
			if(isEmpty(params[i])) {
				isValid = false;
				message += "<br>"+var_names[i]+" cannot be empty";
			}
		}
		/*if(isEmpty(firstname)) {
			isValid = false; 
			message += "First name cannot be empty";
		}*/
		if(hasSpecial(firstname)||hasSpecial(lastname)) {
			isValid = false;
			message += "<br>The password must contain at least 1 special character";
		}
		if(!isEmailValid(email)) {
			isValid = false;
			message += "<br>You must enter a valid email address";
		}
		if(!isPasswordValid(password)) {
			isValid = false;
			message += "<br>Your password must be 6 characters long";
		}
		if(!password.equals(passwordConfirm)) {
			isValid = false;
			message += "<br>Your passwords don't match";
		}
		if(request.getParameter("agree_check") == null) {
			isValid = false;
			message += "<br>You must agree to our terms of service";
		}
		if(isValid) {
			message += "Successfully Registered User<br>An Email has been sent to "+email+
					". Please check your email to verify and confirm";
			color = "green";
		}else {color="red";}
		request.setAttribute("color", color);
		request.setAttribute("statusMessage", message);
		request.getRequestDispatcher("registration.jsp").include(request,response);
		
		
	}
	
	protected boolean hasSpecial(String s) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if(b) {
			return true;
		}
		return false;
	}
	protected boolean isEmpty(String s) {
		if(s == null||s.trim().isEmpty()||s.equals("")) {
			return true;
		}
		return false;
	}
	protected boolean isEmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ //PATTERN FOR EMAIL VALIDATION
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); //RETURNS A BOOLEAN
	}
	protected boolean isPasswordValid(String password) {
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