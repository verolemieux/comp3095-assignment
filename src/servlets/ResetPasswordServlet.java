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
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDao user = new UserDao();
		String username = request.getParameter("username");
		String key = request.getParameter("key");
		if (key != null) {
			String newPassword = request.getParameter("newPassword");
			try {
				if (!user.userExists(username)) {
					request.setAttribute("statusMessage", "Invalid Username");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else if (!user.keyMatchesUser(username, key)) {
					request.setAttribute("statusMessage", "Check your email for the link to reset your password");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else if (!user.isPasswordValid(newPassword)) {
					request.setAttribute("statusMessage", "Your password must be 6-12 characters in length, and must contain at least 1 uppercase letter and 1 special character");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else {
					user.resetPassword(username, newPassword);
					request.getRequestDispatcher("login.jsp").include(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (user.userExists(username)) {
					user.sendResetPasswordEmail(user.getUser(username));
					request.setAttribute("statusMessage", "Link has been sent to your email");
					request.setAttribute("color", "green");
					request.getRequestDispatcher("forgotpassword.jsp").include(request, response);
				} else {
					request.setAttribute("statusMessage", "Invalid username");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("forgotpassword.jsp").include(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}

