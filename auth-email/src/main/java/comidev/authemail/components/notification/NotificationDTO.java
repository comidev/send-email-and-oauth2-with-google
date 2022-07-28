package comidev.authemail.components.notification;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private String toEmail;
    private String subject;
}
