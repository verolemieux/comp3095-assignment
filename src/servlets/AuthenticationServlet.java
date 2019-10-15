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
		if(username == "" || password == "")
		{
			request.getRequestDispatcher("login.jsp").include(request, response);
			return;
		}
		UserDao DBUser = new UserDao();
		try {
			if(DBUser.validateUser(request.getParameter("username").toString(), request.getParameter("password").toString()))
			{
				HttpSession session = request.getSession();
				//set max session under inactivity for 15 minutes
				session.setMaxInactiveInterval(60*15);
				User authUser = DBUser.getUser(request.getParameter("username").toString());
				if(authUser.isVerified() == 1)
				{
					session.setAttribute("authUser", authUser);
					session.setAttribute("LoggedIn", "true");
					request.getRequestDispatcher("dashboard.jsp").forward(request, response);
				}
				else
				{
					System.out.println("Not verified");
					String errorMessage = String.format("A verification email has been sent to %s. Please verify your email.", authUser.getEmail());
					request.setAttribute("errorMessage", errorMessage);
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
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
	}

}
