package mercado_financeiro.listener;

import mercado_financeiro.event.InvestorCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

public class InvestorWelcomeListener {

    private static final Logger log = LoggerFactory.getLogger(InvestorWelcomeListener.class);

    @EventListener
    public void onInvestorCreated(InvestorCreatedEvent event) {
        log.info(
                "BOAS-VINDAS: preparando mensagem para {} ({})",
                event.name(),
                event.email()
        );
    }
}
