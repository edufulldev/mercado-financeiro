package mercado_financeiro.repository;

import mercado_financeiro.entity.InvestmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentOrderRepository extends JpaRepository<InvestmentOrder, Long> {
    List<InvestmentOrder> findByInvestor_Id(Long investorId);
}
