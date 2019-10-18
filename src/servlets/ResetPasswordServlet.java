/*********************************************************************************
* Project: < ABC Financial Institution >
* Assignment: < 1>
* Author(s): < Jeremy Thibeau, Veronyque Lemieux, Sergio Lombana, Ian Miranda>
* Student Number: < 101157911, 101106553, 101137768, 101163981>
* Date: October 18, 2019
* Description: Handles requests coming through the Reset mapping. If user has not had the email generated to reset password, generates it. 
* Otherwise validates reset key and username, as well as password contents, to determine if password reset can be completed. 
*********************************************************************************/

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
			//if user clicks link in password reset email
			String newPassword = request.getParameter("newPassword");
			try {
				if (!user.userExists(username)) {
					//checks for username being correct
					request.setAttribute("statusMessage", "Invalid Username");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else if (!user.keyMatchesUser(username, key)) {
					//checks to ensure key matches key generated to email
					request.setAttribute("statusMessage", "Check your email for the link to reset your password");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else if (!user.isPasswordValid(newPassword)) {
					//validates password matches criteria
					request.setAttribute("statusMessage", "Your password must be 6-12 characters in length, and must contain at least 1 uppercase letter and 1 special character");
					request.setAttribute("color", "red");
					request.getRequestDispatcher("resetpassword.jsp").include(request, response);
				} else {
					//completes password reset
					user.resetPassword(username, newPassword);
					request.setAttribute("statusMessage",  "Password reset. Please login.");
					request.setAttribute("color", "green");
					request.getRequestDispatcher("login.jsp").include(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//if user clicks on reset password
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

