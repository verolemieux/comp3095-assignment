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
		if(session.getAttribute("LoggedIn") == null)
		{
			String errorMessage = "Please log in.";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("login.jsp").include(request, response);
		}	
		String buttonAction = request.getParameter("button");
		
		if(request.getParameter("Logout") != null)
		{
			log(request.getParameter("Logout").toString());
			session.invalidate();
			String errorMessage = "Logged out successfully.";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("login.jsp").include(request, response);	
		}	
		else if(!"login".contentEquals(null))
		{
			if("login".contentEquals(buttonAction)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			if(valid)
			{
				request.getRequestDispatcher("Auth").include(request, response);
			}
			else
			{
				String errorMessage = "Please verify recaptcha.";
				request.setAttribute("errorMessage", errorMessage);
				request.getRequestDispatcher("login.jsp").include(request, response);
			}
		}
		}
		
		else if("register".contentEquals(buttonAction)) {
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}	
	}
}
