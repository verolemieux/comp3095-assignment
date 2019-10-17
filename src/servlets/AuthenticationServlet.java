package servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Role;
import beans.User;
import dao.UserDao;
import dao.UserRoleDao;
import recaptcha.VerifyUtils;

@WebServlet("/Auth")
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AuthenticationServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		if(username == "" || password == "")
		{
			request.getRequestDispatcher("login.jsp").include(request, response);
			return;
		}
		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
		if(!valid)
		{
			String errorMessage = "Please verify recaptcha.";
			request.setAttribute("statusMessage", errorMessage);
			request.setAttribute("color", "red");
			request.getRequestDispatcher("login.jsp").include(request, response);
			return;
		}
		else
		{
			UserDao DBUser = new UserDao();
			try {
				if(DBUser.validateUser(username, password))
				{
					HttpSession session = request.getSession();
					//set max session under inactivity for 15 minutes
					session.setMaxInactiveInterval(60*15);
					User authUser = DBUser.getUser(request.getParameter("username").toString());
					Role[] userRoles;
					UserRoleDao getRoles = new UserRoleDao();
					authUser.setRole(getRoles.getRoles(authUser.getId()));
					if(authUser.getVerified() == 1)
					{
						session.setAttribute("authUser", authUser);
						session.setAttribute("LoggedIn", "true");
						String verificationKey = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
						System.out.println("Generated key: " + verificationKey);
						authUser.setVerificationkey(verificationKey);
						System.out.println("Stored key: " + authUser.getVerificationkey());
						DBUser.updateKey(authUser.getId(), verificationKey);
						request.getRequestDispatcher("dashboard.jsp").forward(request, response);
					}
					else
					{
						String key = request.getParameter("key");
						if (key != null && DBUser.keyMatchesUser(username, key)) {
							DBUser.verifyEmail(authUser, key);
							session.setAttribute("authUser", authUser);
							session.setAttribute("LoggedIn", "true");
							request.getRequestDispatcher("dashboard.jsp").forward(request, response);
						} else {
	                        //If user isn't verified, generates message and button to have email resent
	                        String errorMessage = String.format("A verification email has been sent to %s. Please verify your email.", authUser.getEmail());
	                        String errorMessage2 = String.format("<form action=\"Login\" method=\"post\"><div class=\"p-t-10 buttons-container\">\n" +
	                                "<button class=\"btn btn--pill btn--blue\" name=\"button\" value=\"resend\" type=\"submit\">Resend</button></div></form>");
	                        request.setAttribute("statusMessage", errorMessage);
	                        request.setAttribute("statusMessage2", errorMessage2);
	                        request.setAttribute("color", "red");
	                        request.getRequestDispatcher("login.jsp").forward(request, response);
	                    }
					}
				}
				else
				{
					String errorMessage = "Invalid username and/or password";
					request.setAttribute("statusMessage", errorMessage);
					request.setAttribute("color", "red");
					request.getRequestDispatcher("login.jsp").include(request, response);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

}
