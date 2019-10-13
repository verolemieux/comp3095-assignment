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
		String buttonAction = request.getParameter("button");
		
		if(request.getParameter("Logout") != null)
		{
			log(request.getParameter("Logout").toString());
			String errorMessage = "Logged out successfully.";
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("login.jsp").include(request, response);
			
		}
		else if("login".contentEquals(buttonAction)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			UserDao DBUser = new UserDao();
			try {
				if(DBUser.validateUser(request.getParameter("username").toString(), request.getParameter("password").toString()))
				{
					request.setAttribute("name", username);
					request.getRequestDispatcher("dashboard.jsp").forward(request, response);
				}
				else
				{
					String errorMessage = "Invalid username and/or password";
					request.setAttribute("errorMessage", errorMessage);
					request.getRequestDispatcher("login.jsp").include(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/*//to be migrated to UserDao
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			//username and password will come from database
			if ("admin@isp.net".equals(username) && "P@ssword1".equals(password) && valid) {
				request.setAttribute("name", "admin");
				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			} else {
				String errorMessage = "Invalid username and/or password";
				request.setAttribute("errorMessage", errorMessage);
				request.getRequestDispatcher("login.jsp").include(request, response);
			}*/
		} else if("register".contentEquals(buttonAction)) {
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}	
	}
}
