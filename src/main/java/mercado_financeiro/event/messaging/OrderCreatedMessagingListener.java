package mercado_financeiro.event.messaging;

import mercado_financeiro.event.OrderCreatedEvent;
import mercado_financeiro.event.OrderCreatedInternalEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderCreatedMessagingListener {

    private final OrderEventPublisher publisher;

    public OrderCreatedMessagingListener(OrderEventPublisher publisher) {
        this.publisher = publisher;
    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedInternalEvent event) {
        OrderCreatedEvent message = new OrderCreatedEvent(
                event.orderId(),
                event.investorId(),
                event.assetSymbol(),
                event.type(),
                event.quantity(),
                event.grossAmount(),
                event.fee(),
                event.totalAmount(),
                event.createdAt()
        );
        publisher.publish(message);
    }
}
