package pl.edu.pw.restapi.service.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.User;
import pl.edu.pw.restapi.service.payu.dto.PayuNotification;
import pl.edu.pw.restapi.service.payu.dto.PayuResponse;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final EmailService emailService;

    public void sendRegistrationNotification(User user) {
        try {
            String subject = "Welcome to the course shop!";
            String content = "Hi " + user.getUsername() + ",\n" +
                    "Welcome to the course shop, we hope you like it!";

            emailService.sendEmail(user.getEmail(), subject, content);
        } catch (MessagingException e) {
            log.error("Could not send registration notification to " + user.getEmail(), e);
        }
    }

    public void sendPaymentNotification(User user, Course course, PayuResponse payuResponse) {
        try {
            String subject = "Course shop - new payment registered";
            String content = "Hi " + user.getUsername() + ",\n" +
                    "We registered new payment in our system.\n" +
                    "Details: \n" +
                    "Course id: " + course.getId() + "\n" +
                    "Course title: " + course.getTitle() + "\n" +
                    "Payment id: " + payuResponse.getOrderId() + "\n" +
                    "Payment link: " + payuResponse.getRedirectUri();

            emailService.sendEmail(user.getEmail(), subject, content);
        } catch (MessagingException e) {
            log.error("Could not send payment notification to " + user.getEmail(), e);
        }
    }

    public void sendPurchaseNotification(User user, Course course, PayuNotification payuNotification) {
        String subject = "Course shop - new purchase registered";
        String content = "Hi " + user.getUsername() + ",\n" +
                "We registered new purchase in our system.\n" +
                "Details: \n" +
                "Course id: " + course.getId() + "\n" +
                "Course title: " + course.getTitle();

        if (payuNotification != null) {
            PayuNotification.Order order = payuNotification.getOrder();
            content += "\n" +
                    "Payment id: " + order.getOrderId() + "\n" +
                    "Payment date: " + order.getOrderCreateDate() + "\n" +
                    "Payment amount: " + order.getTotalAmount() + " " + order.getCurrencyCode();
        }

        try {
            emailService.sendEmail(user.getEmail(), subject, content);
        } catch (MessagingException e) {
            log.error("Could not send purchase notification to " + user.getEmail(), e);
        }
    }

    public void sendResetPasswordNotification(User user, String newPassword) {
        String subject = "Course shop - password reset";
        String content = "Hi " + user.getUsername() + ",\n" +
                "Your new password: " + newPassword;

        try {
            emailService.sendEmail(user.getEmail(), subject, content);
        } catch (MessagingException e) {
            log.error("Could not send password reset notification to " + user.getEmail(), e);
        }
    }

}
