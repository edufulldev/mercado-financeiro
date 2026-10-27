package mercado_financeiro.listener;

import mercado_financeiro.event.InvestorCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

public class InvestorAuditListener {

    private static final Logger log = LoggerFactory.getLogger(InvestorAuditListener.class);

    @EventListener
    public void onInvestorCreated(InvestorCreatedEvent event) {
        log.info(
                "AUDITORIA: investidor cadastrado. ID={}, email={}, data={}",
                event.investorId(),
                event.email(),
                event.occurredAt()
        );
    }
}
