package comidev.notification.consumer.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import comidev.notification.components.email.EmailSenderService;
import comidev.notification.components.notification.dto.NotificationCreate;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Consumer {
    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = { "${comidev.queue.email}" })
    private void recieve(NotificationCreate message) {
        emailSenderService.send(message);
    }
}
