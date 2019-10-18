package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.UserDao;
import recaptcha.VerifyUtils;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		//redirect to here if client attempts to access a page they need to be logged in for
		if(request.getAttribute("GuestUser") != null)
		{
			if(request.getAttribute("GuestUser").equals("true"))
			{
				String errorMessage = "Please log in.";
				request.setAttribute("statusMessage", errorMessage);
				request.setAttribute("color", "red");
				request.getRequestDispatcher("login.jsp").include(request, response);
			}	
		}
		String buttonAction = request.getParameter("button");
		String username = request.getParameter("username");
		request.setAttribute("username", username);
		
		if(request.getParameter("Logout") != null)
		{
			//if user clicks to log out
			log(request.getParameter("Logout").toString());
			session.invalidate();
			String errorMessage = "Logged out successfully.";
			request.setAttribute("statusMessage", errorMessage);
			request.setAttribute("color", "green");
			request.getRequestDispatcher("login.jsp").include(request, response);	
		}	
		else if("resend".contentEquals(buttonAction))
		{
			UserDao userDao = new UserDao();
			try {
				//if user clicks to resend verification email
				User user = userDao.getUser(username);
				userDao.sendEmailVerificationEmail(user);
				String errorMessage = "Verification email resent.";
				request.setAttribute("statusMessage", errorMessage);
				request.setAttribute("color", "green");
				request.getRequestDispatcher("login.jsp").include(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if("login".contentEquals(buttonAction)) {
			//if user clicked to login
			request.getRequestDispatcher("Auth").include(request, response);
		}	
		else if("register".contentEquals(buttonAction)) {
			//if user clicked to register
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}
	}
}
