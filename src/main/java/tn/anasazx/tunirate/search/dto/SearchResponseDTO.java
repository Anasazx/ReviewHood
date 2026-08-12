package tn.anasazx.tunirate.search.dto;

import org.springframework.data.domain.Page;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.product.dto.ProductResponse;



public record SearchResponseDTO(
    Page<ProductResponse> products,
    Page<CompanyResponse> companies
){}