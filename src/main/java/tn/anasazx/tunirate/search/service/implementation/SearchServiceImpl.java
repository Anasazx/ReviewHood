package tn.anasazx.tunirate.search.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.search.searchDTO.SearchResponseDTO;
import tn.anasazx.tunirate.search.service.SearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductService productService;
    private final CompanyService companyService;

    @Override
    public SearchResponseDTO search(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return new SearchResponseDTO(List.of(), List.of());
        }

        List<ProductResponse> products = productService.search(q);
        List<CompanyResponse> companies = companyService.search(q);

        return new SearchResponseDTO(products, companies);
    }


}
