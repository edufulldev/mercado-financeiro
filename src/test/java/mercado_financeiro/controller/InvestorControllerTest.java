package mercado_financeiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mercado_financeiro.dto.CreateInvestorRequest;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.exception.GlobalExceptionHandler;
import mercado_financeiro.exception.InvestorNotFoundException;
import mercado_financeiro.service.InvestorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestorController.class)
@Import(GlobalExceptionHandler.class)
class InvestorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvestorService investorService;

    @Test
    void shouldReturn201WhenInvestorIsCreated() throws Exception {
        // O serviço retorna a Entidade Investor
        Investor investor = new Investor(
                1L,
                "Joao Silva",
                "joao@example.com",
                LocalDateTime.now()
        );

        when(investorService.create(any(CreateInvestorRequest.class)))
                .thenReturn(investor);

        mockMvc.perform(post("/api/investors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateInvestorRequest(
                                        "Joao Silva",
                                        "joao@example.com"
                                )
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Joao Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/api/investors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Joao Silva",
                                  "email": "email-invalido"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(investorService);
    }

    @Test
    void shouldReturn404WhenInvestorDoesNotExist() throws Exception {
        when(investorService.findById(99L))
                .thenThrow(new InvestorNotFoundException(99L));

        mockMvc.perform(get("/api/investors/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnInvestorList() throws Exception {
        // O serviço retorna uma Lista de Entidades Investor
        when(investorService.findAll()).thenReturn(List.of(
                new Investor(
                        1L,
                        "Joao Silva",
                        "joao@example.com",
                        LocalDateTime.now()
                )
        ));

        mockMvc.perform(get("/api/investors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Joao Silva"));
    }
}