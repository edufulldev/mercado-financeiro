package mercado_financeiro.strategy;

import mercado_financeiro.entity.OrderType;

import java.math.BigDecimal;

public interface OrderStrategy {

    OrderType getType();

    BigDecimal calculateFee(BigDecimal grossAmount);
}
