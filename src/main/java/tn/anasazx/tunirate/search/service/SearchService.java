package tn.anasazx.tunirate.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.search.dto.SearchResponseDTO;
import tn.anasazx.tunirate.search.dto.SearchSuggestionDTO;

import java.util.List;

public interface SearchService {
    SearchResponseDTO search(String query, Pageable pageable);
    Page<ProductResponse> searchProducts(String query, Pageable pageable);
    Page<CompanyResponse> searchCompanies(String query, Pageable pageable);
    List<SearchSuggestionDTO> suggestions(String query);
}
