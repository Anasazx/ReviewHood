package tn.anasazx.tunirate.product.dto;

public record ProductImageResponse(
        Long id,
        String url,
        boolean isMain
) {}