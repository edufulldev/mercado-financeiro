package mercado_financeiro.service;

import mercado_financeiro.dto.CreateOrderRequest;
import mercado_financeiro.dto.InvestmentOrderResponse;
import mercado_financeiro.entity.Asset;
import mercado_financeiro.entity.InvestmentOrder;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.entity.OrderType;
import mercado_financeiro.exception.AssetNotFoundException;
import mercado_financeiro.exception.InvestorNotFoundException;
import mercado_financeiro.repository.AssetRepository;
import mercado_financeiro.repository.InvestmentOrderRepository;
import mercado_financeiro.repository.InvestorRepository;
import mercado_financeiro.strategy.BuyOrderStrategy;
import mercado_financeiro.strategy.OrderStrategy;
import mercado_financeiro.strategy.SellOrderStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher; // <-- Import necessário

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentOrderServiceTest {

    @Mock
    private InvestmentOrderRepository orderRepository;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher; // <-- Mock adicionado

    private InvestmentOrderService orderService;

    private Investor investor;
    private Asset asset;

    @BeforeEach
    void setUp() {
        List<OrderStrategy> strategies = List.of(
                new BuyOrderStrategy(),
                new SellOrderStrategy()
        );

        // Passando o eventPublisher como 5º argumento
        orderService = new InvestmentOrderService(
                orderRepository,
                investorRepository,
                assetRepository,
                strategies,
                eventPublisher
        );

        investor = new Investor(
                "Joao Silva",
                "joao.teste@example.com"
        );

        asset = new Asset(
                "TEST4",
                "Empresa de Teste",
                new BigDecimal("32.50")
        );
    }

    @Test
    void shouldCreateBuyOrderSuccessfully() {
        when(investorRepository.findById(1L))
                .thenReturn(Optional.of(investor));

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(orderRepository.save(any(InvestmentOrder.class)))
                .thenAnswer(invocation -> {
                    InvestmentOrder order = invocation.getArgument(0);
                    return order;
                });

        CreateOrderRequest request = new CreateOrderRequest(
                1L, 1L, OrderType.BUY, 10
        );

        InvestmentOrderResponse response =
                orderService.create(request);

        assertEquals(OrderType.BUY, response.type());
        assertEquals(10, response.quantity());
        assertEquals(
                new BigDecimal("325.00"),
                response.grossAmount()
        );
        assertEquals(
                new BigDecimal("0.33"),
                response.fee()
        );
        assertEquals(
                new BigDecimal("325.33"),
                response.totalAmount()
        );

        verify(orderRepository).save(any(InvestmentOrder.class));
    }

    @Test
    void shouldCreateSellOrderSuccessfully() {
        when(investorRepository.findById(1L))
                .thenReturn(Optional.of(investor));

        when(assetRepository.findById(1L))
                .thenReturn(Optional.of(asset));

        when(orderRepository.save(any(InvestmentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderRequest request = new CreateOrderRequest(
                1L, 1L, OrderType.SELL, 10
        );

        InvestmentOrderResponse response =
                orderService.create(request);

        assertEquals(OrderType.SELL, response.type());
        assertEquals(
                new BigDecimal("0.65"),
                response.fee()
        );
        assertEquals(
                new BigDecimal("324.35"),
                response.totalAmount()
        );
    }

    @Test
    void shouldThrowWhenInvestorDoesNotExist() {
        when(investorRepository.findById(99L))
                .thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                99L, 1L, OrderType.BUY, 10
        );

        assertThrows(
                InvestorNotFoundException.class,
                () -> orderService.create(request)
        );

        verifyNoInteractions(assetRepository, orderRepository, eventPublisher);
    }

    @Test
    void shouldThrowWhenAssetDoesNotExist() {
        when(investorRepository.findById(1L))
                .thenReturn(Optional.of(investor));

        when(assetRepository.findById(99L))
                .thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                1L, 99L, OrderType.BUY, 10
        );

        assertThrows(
                AssetNotFoundException.class,
                () -> orderService.create(request)
        );

        verify(orderRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}