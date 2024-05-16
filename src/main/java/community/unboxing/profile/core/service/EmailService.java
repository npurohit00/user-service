package community.unboxing.profile.core.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailSignUpVerification(String email, String otp) {
        String subject = "Unboxing Community - Registration Verification Required!";
        String body = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
          "<meta charset=\"UTF-8\">\n" +
          "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
          "<title>Unboxing Community OTP Verification</title>\n" +
          "<style>\n" +
            "body {\n" +
              "font-family: sans-serif;\n" +
              "background-color: #333; /* Black background */\n" +
              "color: #f80; /* Orange text */\n" +
              "text-align: center; /* Center alignment */\n" +
              "padding: 20px;\n" +
            "}\n" +
            ".otp-box {\n" +
              "background-color: #f80; /* Orange box */\n" +
              "color: #333; /* Black text */\n" +
              "padding: 10px;\n" +
              "margin: 15px auto;\n" +
              "border-radius: 5px;\n" +
              "width: 100px;\n" +
              "text-align: center;\n" +
              "font-size: 20px;\n" +
              "font-weight: bold;\n" +
            "}\n" +
          "</style>\n" +
        "</head>\n" +
        "<body>\n" +
          "<h1>Your Unboxing Community Signup OTP for email verification:</h1>\n" +
          "<div class=\"otp-box\"> " + otp  + " </div>\n" +
        "</body>\n" +
        "</html>";

        sendEmail(email, subject, body, true);
    }

    public void sendEmailVerification(String email, String otp) {
        String subject = "Unboxing Community - Email Verification OTP";
        String body = "Your Unboxing Community OTP for verification is: ".concat(otp);
        sendEmail(email, subject, body, false);
    }

    public void sendEmailVerificationConfirmation(String email, String status) {
        String subject = "Unboxing Community - Email Verification Confirmation";
        String body = "Your Unboxing Community email verification is: ".concat(status);
        sendEmail(email, subject, body, false);
    }

    public void sendEmailSignUpConfirmation(String email, String name) {
        String subject = "Welcome to the Unboxing Community";
        String body = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
          "<meta charset=\"UTF-8\">\n" +
          "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
          "<title>Welcome to the Unboxing Community!</title>\n" +
        "</head>\n" +
        "<body>\n" +
          "<p>Dear <b> " +  name + "</b>,</p>\n" +
          "<p>We're thrilled to welcome you to the Unboxing Community! </p>\n" +
          "<p>Your user profile has been approved by our admins, and your account is now ready to use.</p>\n" +
          "<p>Get ready to explore the vast world of electronics and contribute your valuable insights to our passionate community. We can't wait to see what you bring to the table!</p>\n" +
          "<p>Login now and start connecting with fellow tech enthusiasts: <a href=\"\">Login Page</a></p>\n" +
          "<p>We're excited to have you on board!</p>\n" +
          "<p>Sincerely,</p>\n" +
          "<p>The Unboxing Community Team</p>\n" +
        "</body>\n" +
        "</html>";

        sendEmail(email, subject, body, true);
    }

    @Async
    public void sendEmail(String email, String subject, String body, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, isHtml);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Send Email Exception", e);
        }
    }
}
