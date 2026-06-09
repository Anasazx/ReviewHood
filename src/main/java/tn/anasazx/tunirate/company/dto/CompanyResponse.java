package tn.anasazx.tunirate.company.dto;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl,
        String bannerUrl,
        Boolean verified
) {
}

