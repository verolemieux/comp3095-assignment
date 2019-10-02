package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import recaptcha.VerifyUtils;

@WebServlet("/LoginAuth")

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log("servlet called");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();	
		String buttonAction = request.getParameter("button");
		
		if("login".contentEquals(buttonAction)) {
			//to be migrated to UserDao
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			//username and password will come from database
			if ("admin@isp.net".equals(username) && "P@ssword1".equals(password) && valid) {
				request.getRequestDispatcher("dashboard.html").forward(request, response);
			} else {
				out.print("<h2>Invalid username and/or password</h2>");
				request.getRequestDispatcher("login.html").include(request, response);
			}
		} else if("register".contentEquals(buttonAction)) {
			request.getRequestDispatcher("registration.html").forward(request, response);
		}	
	}
}
