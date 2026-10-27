package mercado_financeiro.controller;

import jakarta.validation.Valid;
import mercado_financeiro.dto.CreateOrderRequest;
import mercado_financeiro.dto.InvestmentOrderResponse;
import mercado_financeiro.service.InvestmentOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class InvestmentOrderController {

    private final InvestmentOrderService orderService;

    public InvestmentOrderController(InvestmentOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentOrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }
    @GetMapping("/investor/{investorId}")
    public List<InvestmentOrderResponse> findByInvestor(@PathVariable Long investorId) {
        return orderService.findByInvestor(investorId);
    }
}
