package mercado_financeiro.exception;

public class AssetAlreadyRegisteredException extends RuntimeException{

    public AssetAlreadyRegisteredException(String symbol) {
        super("Já existe um ativo cadastrado com o símbolo: " + symbol);
    }
}
