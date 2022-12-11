package pl.edu.pw.restapi.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

//    private final boolean auth;
//    private final String enableStarttls;
//    private final String host;
//    private final String port;
//    private final String sslTrust;
//    private final String username;
//    private final String password;
    private final Session session;

    public EmailService(@Value("${mail.smtp.auth}") boolean auth,
                        @Value("${mail.smtp.starttls.enable}") boolean enableStarttls,
                        @Value("${mail.smtp.host}") String host,
                        @Value("${mail.smtp.port}") String port,
                        @Value("${mail.smtp.ssl.trust}") String sslTrust,
                        @Value("${mail.smtp.username}") String username,
                        @Value("${mail.smtp.password}") String password,
                        @Value("${mail.smtp.ssl.protocols}") String protocols) {
//        this.auth = auth;
//        this.enableStarttls = enableStarttls;
//        this.host = host;
//        this.port = port;
//        this.sslTrust = sslTrust;
//        this.username = username;
//        this.password = password;
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", auth);
        prop.put("mail.smtp.starttls.enable", enableStarttls);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", sslTrust);
        prop.put("mail.smtp.ssl.protocols", protocols);

        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("courseshop@noreply.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }

}
