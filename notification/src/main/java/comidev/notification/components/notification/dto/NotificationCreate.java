package comidev.notification.components.notification.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreate implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private String toEmail;
    private String subject;
}
