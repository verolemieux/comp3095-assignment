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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		/*
		 * try { UserDao.connectDataBase(); log("Database connection succesful!"); }
		 * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
		 * }
		 * 
		 * try { user.readDataBase(); } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		for (int i = 0; i < params.length; i++) {
			if (user.isEmpty(params[i])) {
				isValid = false;
				message += "<br>" + var_names[i] + " cannot be empty";
			}
		}
		try {
			if(user.userExists(email))
			{
				isValid = false;
				message += "This email address is already taken.";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (user.hasSpecial(firstname) || user.hasSpecial(lastname)) {
			isValid = false;
			message += "<br>Your first and last name must contain only letters";
		}
		if (!user.isEmailValid(email)) {
			isValid = false;
			message += "<br>You must enter a valid email address";
		}
		if (!user.isPasswordValid(password)) {
			isValid = false;
			message += "<br>Your password must be 6-12 characters in length, and must contain "
					+ "at least 1 uppercase letter and 1 special character";
		}
		if (!password.equals(passwordConfirm)) {
			isValid = false;
			message += "<br>Your passwords don't match";
		}
		if (request.getParameter("agree_check") == null) {
			isValid = false;
			message += "<br>You must agree to our terms of service";
		}
		if (isValid) {
			try {
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
		request.setAttribute("color", color);
		request.setAttribute("statusMessage", message);
		request.getRequestDispatcher("registration.jsp").include(request, response);

	}

}