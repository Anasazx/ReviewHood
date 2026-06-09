package tn.anasazx.tunirate.search.searchDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.product.dto.ProductResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDTO {
    private List<ProductResponse> products;
    private List<CompanyResponse> companies;
}