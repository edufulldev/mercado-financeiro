
package mercado_financeiro.event.messaging;

import mercado_financeiro.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventConsumer.class);

    @RabbitListener(queues = "investhub.orders.queue")
    public void consume(OrderCreatedEvent event) {

        log.info(
                "PROCESSANDO EVENTO | orderId={} | asset={}",
                event.orderId(),
                event.assetSymbol()
        );

        throw new RuntimeException(
                "Falha simulada no processamento da ordem"
        );
    }
}
