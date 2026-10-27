package mercado_financeiro.service;

import mercado_financeiro.dto.CreateOrderRequest;
import mercado_financeiro.dto.InvestmentOrderResponse;
import mercado_financeiro.entity.Asset;
import mercado_financeiro.entity.InvestmentOrder;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.entity.OrderType;
import mercado_financeiro.event.OrderCreatedInternalEvent;
import mercado_financeiro.exception.AssetNotFoundException;
import mercado_financeiro.exception.InvestorNotFoundException;
import mercado_financeiro.repository.AssetRepository;
import mercado_financeiro.repository.InvestmentOrderRepository;
import mercado_financeiro.repository.InvestorRepository;
import mercado_financeiro.strategy.OrderStrategy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestmentOrderService {

    private final InvestmentOrderRepository orderRepository;
    private final InvestorRepository investorRepository;
    private final AssetRepository assetRepository;
    private final Map<OrderType, OrderStrategy> strategies;
    private final ApplicationEventPublisher eventPublisher;

    public InvestmentOrderService(
            InvestmentOrderRepository orderRepository,
            InvestorRepository investorRepository,
            AssetRepository assetRepository,
            List<OrderStrategy> strategies, ApplicationEventPublisher eventPublisher) {

        this.orderRepository = orderRepository;
        this.investorRepository = investorRepository;
        this.assetRepository = assetRepository;
        this.eventPublisher = eventPublisher;
        this.strategies = new EnumMap<>(OrderType.class);

        for (OrderStrategy strategy : strategies) {
            this.strategies.put(strategy.getType(), strategy);
        }
    }

    @Transactional
    public InvestmentOrderResponse create(CreateOrderRequest request) {

        Investor investor = investorRepository
                .findById(request.investorId())
                .orElseThrow(() ->
                        new InvestorNotFoundException(
                                request.investorId()));

        Asset asset = assetRepository
                .findById(request.assetId())
                .orElseThrow(() ->
                        new AssetNotFoundException(
                                request.assetId()));

        if (request.quantity() == null || request.quantity() <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero.");
        }

        OrderStrategy strategy = strategies.get(request.type());

        if (strategy == null) {
            throw new IllegalStateException(
                    "Não existe estratégia para o tipo de ordem: "
                            + request.type());
        }

        BigDecimal unitPrice = asset.getPrice();

        BigDecimal grossAmount = unitPrice
                .multiply(BigDecimal.valueOf(request.quantity()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal fee = strategy.calculateFee(grossAmount);

        BigDecimal totalAmount;

        if (request.type() == OrderType.BUY) {
            totalAmount = grossAmount.add(fee);
        } else {
            totalAmount = grossAmount.subtract(fee);
        }

        InvestmentOrder order = new InvestmentOrder(
                investor,
                asset,
                request.type(),
                request.quantity(),
                unitPrice,
                grossAmount,
                fee,
                totalAmount
        );


        InvestmentOrder savedOrder = orderRepository.save(order);

        eventPublisher.publishEvent(
                new OrderCreatedInternalEvent(
                        savedOrder.getId(),
                        savedOrder.getInvestor().getId(),
                        savedOrder.getAsset().getSymbol(),
                        savedOrder.getType(),
                        savedOrder.getQuantity(),
                        savedOrder.getGrossAmount(),
                        savedOrder.getFee(),
                        savedOrder.getTotalAmount(),
                        savedOrder.getCreatedAt()
                )
        );

        return InvestmentOrderResponse.from(savedOrder);

    }

    @Transactional(readOnly = true)
    public List<InvestmentOrderResponse> findByInvestor(
            Long investorId) {

        if (!investorRepository.existsById(investorId)) {
            throw new InvestorNotFoundException(investorId);
        }

        return orderRepository.findByInvestor_Id(investorId)
                .stream()
                .map(InvestmentOrderResponse::from)
                .toList();
    }
}
