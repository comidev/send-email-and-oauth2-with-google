package comidev.notification.components.notification;

import org.springframework.stereotype.Service;

import comidev.notification.components.notification.dto.NotificationCreate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepo notificationRepo;

    public void saveNotification(NotificationCreate create) {
        Notification notificationNEW = new Notification(create);
        Notification notiDB = notificationRepo.save(notificationNEW);
        log.info(":D Notificacion enviada a -> {}", notiDB.getToEmail());
    }
}
