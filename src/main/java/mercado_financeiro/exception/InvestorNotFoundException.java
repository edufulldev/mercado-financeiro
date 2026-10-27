package mercado_financeiro.exception;

public class InvestorNotFoundException extends RuntimeException{

    public InvestorNotFoundException(Long id) {
        super("Investidor não encontrado com ID: " + id);
    }
}
