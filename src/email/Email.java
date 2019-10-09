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

public class Email {
	
	public Email() {
		
	}

    public void createEmail(String username, String messageText) {

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
            message.setSubject("Reset Your Password");
            message.setText(messageText);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void createResetPasswordMessageEmail(String username, String userFirstName, String key) {
    	String messageText = "<p>Hi " + userFirstName + ",</p>"
    			+ "<p>Please click"
    			+ "<p><a href=\"http://localhost:8080/comp3095Assignment/resetpassword.jsp?key=" + key + "\"> here </a>"
    			+ "to reset your password</p>";
    	createEmail(username, messageText);			   	
    }
}
