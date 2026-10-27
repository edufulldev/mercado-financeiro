package mercado_financeiro.dto;

import mercado_financeiro.entity.InvestmentOrder;
import mercado_financeiro.entity.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentOrderResponse(
        Long id,
        Long investorId,
        Long assetId,
        String assetSymbol,
        OrderType type,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal grossAmount,
        BigDecimal fee,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
    public static InvestmentOrderResponse from(
            InvestmentOrder order) {

        return new InvestmentOrderResponse(
                order.getId(),
                order.getInvestor().getId(),
                order.getAsset().getId(),
                order.getAsset().getSymbol(),
                order.getType(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getGrossAmount(),
                order.getFee(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}

