package mercado_financeiro.controller;

import jakarta.validation.Valid;
import mercado_financeiro.dto.AssetResponse;
import mercado_financeiro.dto.CreateAssetRequest;
import mercado_financeiro.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponse create(@Valid @RequestBody CreateAssetRequest request) {
        return assetService.create(request);
    }

    @GetMapping
    public List<AssetResponse> fndAll() {
        return assetService.findAll();
    }
}
