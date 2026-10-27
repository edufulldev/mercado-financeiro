package mercado_financeiro.controller;

import jakarta.validation.Valid;
import mercado_financeiro.dto.CreateInvestorRequest;
import mercado_financeiro.dto.InvestorResponse;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.service.InvestorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestorResponse create(@Valid @RequestBody CreateInvestorRequest request) {
        Investor investor = investorService.create(request);
        return InvestorResponse.from(investor);
    }
    @GetMapping("/{id}")
    public InvestorResponse findById(@PathVariable Long id) {
        Investor investor = investorService.findById(id);
        return InvestorResponse.from(investor);
    }
    @GetMapping
    public List<InvestorResponse> findAll() {
        return investorService.findAll()
                .stream()
                .map(InvestorResponse::from)
                .toList();
    }
}
