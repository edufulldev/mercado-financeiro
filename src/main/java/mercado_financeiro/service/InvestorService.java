package mercado_financeiro.service;

import mercado_financeiro.dto.CreateInvestorRequest;
import mercado_financeiro.entity.Investor;
import mercado_financeiro.event.InvestorCreatedEvent;
import mercado_financeiro.exception.EmailAlreadyRegisteredException;
import mercado_financeiro.exception.InvestorNotFoundException;
import mercado_financeiro.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import java.util.List;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final ApplicationEventPublisher eventPublisher;

    public InvestorService(InvestorRepository investorRepository, ApplicationEventPublisher eventPublisher) {
        this.investorRepository = investorRepository;
        this.eventPublisher = eventPublisher;
    }
    @Transactional
    public Investor create(CreateInvestorRequest request){
        if(investorRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        }
        Investor investor = new Investor(request.name(), request.email());

        Investor saveDInvestor = investorRepository.save(investor);

        eventPublisher.publishEvent(new InvestorCreatedEvent(
                saveDInvestor.getId(),
                saveDInvestor.getName(),
                saveDInvestor.getEmail(),
                java.time.LocalDateTime.now()
        ));
        return saveDInvestor;
    }

    @Transactional(readOnly = true)
    public Investor findById(Long id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new InvestorNotFoundException(id));
    }

    @Transactional
    public List<Investor> findAll() {
        return investorRepository.findAll();
    }
}
