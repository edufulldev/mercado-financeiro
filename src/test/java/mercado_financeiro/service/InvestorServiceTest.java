package mercado_financeiro.service;

import mercado_financeiro.dto.CreateInvestorRequest;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.event.InvestorCreatedEvent;
import mercado_financeiro.exception.EmailAlreadyRegisteredException;
import mercado_financeiro.exception.InvestorNotFoundException;
import mercado_financeiro.repository.InvestorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestorServiceTest {

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InvestorService investorService;

    @Test
    void shouldCreateInvestorSuccessfully() {
        CreateInvestorRequest request = new CreateInvestorRequest("Maria Silva",
                "maria@example.com");
        when(investorRepository.existsByEmail(request.email())).thenReturn(false);
        when(investorRepository.save(any(Investor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Investor result = investorService.create(request);

        assertNotNull(result);
        assertEquals("Maria Silva", result.getName());
        assertEquals("maria@example.com", result.getEmail());

        verify(investorRepository).save(any(Investor.class));
        verify(eventPublisher).publishEvent(any(InvestorCreatedEvent.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        CreateInvestorRequest request =  new CreateInvestorRequest(
                "Maria Silva",
                "maria@example.com"
        );
        when(investorRepository.existsByEmail(request.email())).thenReturn(true);
        assertThrows(EmailAlreadyRegisteredException.class, () ->
                investorService.create(request));

        verify(investorRepository, never()).save(any(Investor.class));
    }
    @Test
    void shouldThrowExceptionWhenInvestorDoesNotExist() {
        Long id = 999L;
        when(investorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                InvestorNotFoundException.class,
                () -> investorService.findById(id)
        );
        verify(eventPublisher, never()).publishEvent(any(InvestorCreatedEvent.class));
    }
}