package mercado_financeiro.exception;

public class AssetNotFoundException extends RuntimeException{

    public AssetNotFoundException(Long id) {
        super("Ativo não encontrado com ID: " + id);
    }
}
