package mercado_financeiro.service;

import mercado_financeiro.dto.AssetResponse;
import mercado_financeiro.dto.CreateAssetRequest;
import mercado_financeiro.entity.Asset;
import mercado_financeiro.exception.AssetAlreadyRegisteredException;
import mercado_financeiro.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
    @Transactional
    public AssetResponse create(CreateAssetRequest request) {
        String symbol = request.symbol()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (assetRepository.findBySymbol(symbol).isPresent()) {
            throw new AssetAlreadyRegisteredException(symbol);
        }

        Asset asset = new Asset(
                symbol,
                request.name().trim(),
                request.price()
        );

        return AssetResponse.from(assetRepository.save(asset));
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> findAll() {
        return assetRepository.findAll()
                .stream()
                .map(AssetResponse::from)
                .toList();
    }
}
