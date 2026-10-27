package mercado_financeiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import mercado_financeiro.dto.AssetResponse;
import mercado_financeiro.dto.CreateAssetRequest;
import mercado_financeiro.exception.AssetAlreadyRegisteredException;
import mercado_financeiro.exception.GlobalExceptionHandler;
import mercado_financeiro.service.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetController.class)
@Import(GlobalExceptionHandler.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssetService assetService;

    @Test
    void shouldReturn201WhenAssetIsCreated() throws Exception {
        AssetResponse response = new AssetResponse(
                1L,
                "TEST4",
                "Empresa de Teste",
                new BigDecimal("32.50")
        );

        when(assetService.create(any(CreateAssetRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateAssetRequest(
                                        "TEST4",
                                        "Empresa de Teste",
                                        new BigDecimal("32.50")
                                )
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("TEST4"))
                .andExpect(jsonPath("$.price").value(32.50));
    }

    @Test
    void shouldReturn400WhenPriceIsInvalid() throws Exception {
        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "symbol": "TEST4",
                                  "name": "Empresa de Teste",
                                  "price": 0
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(assetService);
    }

    @Test
    void shouldReturn409WhenAssetAlreadyExists() throws Exception {
        when(assetService.create(any(CreateAssetRequest.class)))
                .thenThrow(new AssetAlreadyRegisteredException("TEST4"));

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "symbol": "TEST4",
                                  "name": "Empresa de Teste",
                                  "price": 32.50
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnAssetList() throws Exception {
        when(assetService.findAll()).thenReturn(List.of(
                new AssetResponse(
                        1L,
                        "TEST4",
                        "Empresa de Teste",
                        new BigDecimal("32.50")
                )
        ));

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("TEST4"));
    }
}
