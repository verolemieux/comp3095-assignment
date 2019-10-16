package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
				request.setAttribute("errorMessage", errorMessage);
				request.getRequestDispatcher("login.jsp").include(request, response);
			}	
		}
		String buttonAction = request.getParameter("button");
		
		if(request.getParameter("Logout") != null)
		{
			//if user clicks to log out
			log(request.getParameter("Logout").toString());
			session.invalidate();
			String errorMessage = "Logged out successfully.";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("login.jsp").include(request, response);	
		}	
		else if("resend".contentEquals(buttonAction))
		{
			//if user clicks to resend verification email
			String errorMessage = "Verification email resent.";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("login.jsp").include(request, response);
		}
		else if("login".contentEquals(buttonAction)) {
			//String username = request.getParameter("username");
			//String password = request.getParameter("password");
			//String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			request.getRequestDispatcher("Auth").include(request, response);
			/*boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			if(valid)
			{
				request.getRequestDispatcher("Auth").include(request, response);
			}
			else
			{
				String errorMessage = "Please verify recaptcha.";
				request.setAttribute("errorMessage", errorMessage);
				request.getRequestDispatcher("login.jsp").include(request, response);
			}*/
		}	
		else if("register".contentEquals(buttonAction)) {
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}	
	}
}
