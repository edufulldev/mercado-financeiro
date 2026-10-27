package mercado_financeiro.dto;

import mercado_financeiro.entity.Investor;

import java.time.LocalDateTime;

public record InvestorResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt
) {
    public static InvestorResponse from(Investor investor) {
        return new InvestorResponse(
                investor.getId(),
                investor.getName(),
                investor.getEmail(),
                investor.getCreatedAt()
        );
    }
}
