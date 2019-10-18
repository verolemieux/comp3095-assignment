package servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;

@WebServlet("/Register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegistrationServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//sanitize input
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("confirm_password");
		String verificationKey = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
		String[] params = new String[] { firstname, lastname, address, email, password, passwordConfirm };
		String[] var_names = new String[] { "First name", "Last name", "Address", "Email", "Password",
				"Confirm Password" }; // MADE AN ARRAY TO VALIDATE FASTER
		String message = "";
		String color = "";
		boolean isValid = true;
		UserDao user = new UserDao();

		//error message
		for (int i = 0; i < params.length; i++) {
			if (user.isEmpty(params[i])) {
				isValid = false;
				message += var_names[i] + " cannot be empty.<br>";
			}
			if(params[i].length() > 255)
			{
				message += var_names[i] + " is too long, please shorten it.<br>";
			}
		}
		try {
			if(user.userExists(email))
			{
				//if email already taken
				isValid = false;
				message += "This email address is already taken.<br>";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (user.hasSpecial(firstname) || user.hasSpecial(lastname)) {
			//checks for special characters
			isValid = false;
			message += "Your first and last name must contain only letters.<br>";
		}
		if (!user.isEmailValid(email)) {
			//checks for valid email address
			isValid = false;
			message += "You must enter a valid email address.<br>";
		}
		if (!user.isPasswordValid(password)) {
			//checks if password matches criteria
			isValid = false;
			message += "Your password must be 6-12 characters in length, and must contain "
					+ "at least 1 uppercase letter and 1 special character.<br>";
		}
		if (!password.equals(passwordConfirm)) {
			//check for password match
			isValid = false;
			message += "Your passwords don't match.<br>";
		}
		if (request.getParameter("agree_check") == null) {
			//check for terms of service agreement
			isValid = false;
			message += "You must agree to our terms of service.<br>";
		}
		if (isValid) {
			try {
				//if all entries valid inserts to DB
				user.insertDB(firstname, lastname, address, email, verificationKey, password);
				message += "Successfully Registered User<br>An Email has been sent to " + email
						+ ". Please check your email to verify and confirm";
				color = "green";
				user.sendEmailVerificationEmail(user.getUser(email));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			color = "red";
		}
		//stick form
		request.setAttribute("firstname", firstname);
		request.setAttribute("lastname", lastname);
		request.setAttribute("address", address);
		request.setAttribute("email", email);
		request.setAttribute("color", color);
		request.setAttribute("statusMessage", message);
		request.getRequestDispatcher("registration.jsp").include(request, response);
	}
}