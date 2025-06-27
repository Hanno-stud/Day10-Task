package org.example;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

class EmailService {
    public static void sendEmail(String toEmail, String password) {
        final String fromEmail = "nivinkanjirathinkal@hotmail.com"; // Replace with your email
        final String emailPassword = "gftz sdhi gcdm isyn";    // Replace with your email app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Elective Course Registration Password");
            message.setText("Your password is: " + password);
            Transport.send(message);
            System.out.println("Password sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.out.println("Failed to send email.");
            e.printStackTrace();
        }
    }
}

