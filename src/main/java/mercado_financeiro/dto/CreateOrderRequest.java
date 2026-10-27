package mercado_financeiro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import mercado_financeiro.entity.OrderType;

public record CreateOrderRequest(

        @NotNull
        Long investorId,

        @NotNull
        Long assetId,

        @NotNull
        OrderType type,

        @NotNull
        @Positive
        Integer quantity
) {
}
