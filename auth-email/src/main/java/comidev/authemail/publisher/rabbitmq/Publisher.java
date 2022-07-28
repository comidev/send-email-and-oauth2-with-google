package comidev.authemail.publisher.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class Publisher {
    private final DirectExchange exchange;
    private final RabbitTemplate rabbitTemplate;

    @Value("${comidev.routing.email}")
    private String ROUTING;

    public Publisher(DirectExchange exchange, RabbitTemplate rabbitTemplate) {
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object message) {
        rabbitTemplate.convertAndSend(exchange.getName(), ROUTING, message);
    }
}
