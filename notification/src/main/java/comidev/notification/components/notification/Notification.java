package comidev.notification.components.notification;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import comidev.notification.components.notification.dto.NotificationCreate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Lob
    private String content;
    @Column(nullable = false, name = "to_email")
    private String toEmail;
    @Column(nullable = false)
    private String subject;

    private LocalDateTime createdAt;

    public Notification(NotificationCreate notificationCreate) {
        this.content = notificationCreate.getContent();
        this.toEmail = notificationCreate.getToEmail();
        this.subject = notificationCreate.getSubject();
        this.createdAt = LocalDateTime.now();
    }
}
