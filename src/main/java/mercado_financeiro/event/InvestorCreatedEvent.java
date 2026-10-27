package mercado_financeiro.event;

import java.time.LocalDateTime;

public record InvestorCreatedEvent(
        Long investorId,
        String name,
        String email,
        LocalDateTime occurredAt
) {
}
