package mercado_financeiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import mercado_financeiro.dto.CreateOrderRequest;
import mercado_financeiro.dto.InvestmentOrderResponse;
import mercado_financeiro.entity.OrderType;
import mercado_financeiro.exception.GlobalExceptionHandler;
import mercado_financeiro.service.InvestmentOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestmentOrderController.class)
@Import(GlobalExceptionHandler.class)
class InvestmentOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvestmentOrderService orderService;

    @Test
    void shouldReturn201WhenOrderIsCreated() throws Exception {
        InvestmentOrderResponse response = new InvestmentOrderResponse(
                1L,
                1L,
                1L,
                "TEST4",
                OrderType.BUY,
                10,
                new BigDecimal("32.50"),
                new BigDecimal("325.00"),
                new BigDecimal("0.33"),
                new BigDecimal("325.33"),
                LocalDateTime.now()
        );

        when(orderService.create(any(CreateOrderRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateOrderRequest(
                                        1L, 1L, OrderType.BUY, 10
                                )
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("BUY"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.totalAmount").value(325.33));
    }

    @Test
    void shouldReturn400WhenQuantityIsInvalid() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "investorId": 1,
                                  "assetId": 1,
                                  "type": "BUY",
                                  "quantity": 0
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    void shouldReturnOrderListForInvestor() throws Exception {
        when(orderService.findByInvestor(1L)).thenReturn(List.of(
                new InvestmentOrderResponse(
                        1L,
                        1L,
                        1L,
                        "TEST4",
                        OrderType.BUY,
                        10,
                        new BigDecimal("32.50"),
                        new BigDecimal("325.00"),
                        new BigDecimal("0.33"),
                        new BigDecimal("325.33"),
                        LocalDateTime.now()
                )
        ));

        mockMvc.perform(get("/api/orders/investor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("BUY"))
                .andExpect(jsonPath("$[0].assetSymbol").value("TEST4"));
    }
}
