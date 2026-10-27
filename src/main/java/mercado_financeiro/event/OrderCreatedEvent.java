package mercado_financeiro.event;

import mercado_financeiro.entity.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long investorId,
        String assetSymbol,
        OrderType type,
        Integer quantity,
        BigDecimal grossAmount,
        BigDecimal fee,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
