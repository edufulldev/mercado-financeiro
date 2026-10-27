package mercado_financeiro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAssetRequest(

        @NotBlank
        @Size(max = 50)
        String symbol,

        @NotBlank
        @Size(max = 120)
        String name,

        @NotNull
        @DecimalMin(value = "0.0001")
        BigDecimal price
) {
}
