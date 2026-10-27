package mercado_financeiro.strategy;

import mercado_financeiro.entity.OrderType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BuyOrderStrategy implements OrderStrategy{

    private static final BigDecimal FEE_RATE =
            new BigDecimal("0.001");

    @Override
    public OrderType getType() {
        return OrderType.BUY;
    }

    @Override
    public BigDecimal calculateFee(BigDecimal grossAmount) {
        return grossAmount.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
