package comidev.notification.components.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import comidev.notification.components.notification.NotificationService;
import comidev.notification.components.notification.dto.NotificationCreate;
import comidev.notification.exceptions.HttpException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final NotificationService notificationService;
    private final static String FROM = "comidev.contacto@gmail.com";

    public EmailSenderService(JavaMailSender mailSender, NotificationService notificationService) {
        this.mailSender = mailSender;
        this.notificationService = notificationService;
    }

    @Async
    public void send(NotificationCreate notification) {
        String text = notification.getContent();
        String to = notification.getToEmail();
        String subject = notification.getSubject();

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(FROM);
            mailSender.send(mimeMessage);
            notificationService.saveNotification(notification);
        } catch (MessagingException e) {
            String message = "Fallo el envio del Email :(";
            log.error(message, e);
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

}
