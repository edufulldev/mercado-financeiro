package mercado_financeiro.dto;

import mercado_financeiro.entity.Asset;

import java.math.BigDecimal;

public record AssetResponse(
        Long id,
        String symbol,
        String name,
        BigDecimal price
) {
    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                asset.getPrice()
        );
    }
}
