package email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import beans.User;

public class Email {
	
	public Email() {
		
	}

    public void createEmail(String username, String subjectText, String messageText) {
    	//template for email creation
        final String myUsername = "abcfinancialinstitution@gmail.com";
        final String myPassword = "comp3095";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myUsername, myPassword);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("abcfinancialinstitution@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(username));
            message.setSubject(subjectText);
            message.setContent(messageText, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void createResetPasswordMessageEmail(User user) {
    	//creates password reset email
    	String subjectText = "Reset Your Password";
    	String messageText = "Hi " + user.getFirstname() + ",<br><br>"
    			+ "Click "
    			+ "<a href=\"http://localhost:8080/comp3095Assignment/resetpassword.jsp?key=" + user.getVerificationkey() + "\">here</a> "
    			+ "to reset your password.<br><br>"
    			+ "Please reply to this email with any questions.<br><br>"
    			+ "Thank you and have a great day,<br><br>ABC Financial Institution";   	
    	createEmail(user.getEmail(), subjectText, messageText);			   	
    }
    
    public void createRegistrationMessageEmail(User user) {
    	//creates email for registration verification
    	String subjectText = "Verify Your Email";
    	String messageText = "Hi " + user.getFirstname() + ",<br><br>"
    			+ "Thank you for registering with ABC Financial Institution!<br><br>"
    			+ "<table><tr><td>First Name:</td><td>" + user.getFirstname() + "</td></tr>"
    			+ "<tr><td>Last Name:</td><td>" + user.getLastname() + "</td></tr>"
    			+ "<tr><td>Email:</td><td>" + user.getEmail() + "</td></tr>"
    			+ "<tr><td>Address:</td><td>" + user.getAddress() + "</td></tr></table><br><br>"
    			+ "Verify your email by logging in "
    			+ "<a href=\"http://localhost:8080/comp3095Assignment/login.jsp?key=" + user.getVerificationkey() + "\">here</a>.<br><br>"
    			+ "Please reply to this email with any questions.<br><br>"
    			+ "Thank you and have a great day,<br><br>ABC Financial Institution";
    	createEmail(user.getEmail(), subjectText, messageText);		 
    }
}
