package mercado_financeiro.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String email) {
        super("Já existe um investidor cadastrado com o email: " + email);
    }
}
