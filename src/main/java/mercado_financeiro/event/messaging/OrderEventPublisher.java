package mercado_financeiro.event.messaging;

import mercado_financeiro.config.RabbitMQConfig;
import mercado_financeiro.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                event
        );
    }
}
