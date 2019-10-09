package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;

@WebServlet("/Reset")
public class ResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ResetPasswordServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDao user = new UserDao();
		String username = request.getParameter("username");
		String newPassword = request.getParameter("newPassword");
		String key = request.getParameter("key");
		
		if (!user.usernameExists(username)) {
			request.setAttribute("resetErrorMessage", "Invalid Username");
			request.getRequestDispatcher("resetpassword.jsp").include(request, response);
		} else if (!user.keyMatchesUser(username, key)) {
			request.setAttribute("resetErrorMessage", "Check your email for the link to reset your password");
			request.getRequestDispatcher("resetpassword.jsp").include(request, response);
		} else if (!user.isPasswordValid(newPassword)) {
			request.setAttribute("resetErrorMessage", "Invalid Password");
			request.getRequestDispatcher("resetpassword.jsp").include(request, response);
		} else {
			user.resetPassword(username, newPassword);
			request.getRequestDispatcher("login.jsp").include(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDao user = new UserDao();
		String username = request.getParameter("username");
		if (user.usernameExists(username)) {
			user.sendResetPasswordEmail(username);		
		} else {
			String emailErrorMessage = "Invalid Username";
			request.setAttribute("emailErrorMessage", emailErrorMessage);
			request.getRequestDispatcher("forgotpassword.jsp").include(request, response);
		}
	}

}
;
