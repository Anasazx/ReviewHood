package tn.anasazx.tunirate.search.mapper;

import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.SearchSuggestionType;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.search.dto.SearchSuggestionDTO;

@Component
public class SearchMapper {

    public SearchSuggestionDTO toResponse(Product product) {
        return new SearchSuggestionDTO(
                product.getId(),
                product.getName(),
                SearchSuggestionType.PRODUCT,
                null,
                product.getCompany().getName()
        );
    }

    public SearchSuggestionDTO toResponse(Company company) {
        return new SearchSuggestionDTO(
                company.getId(),
                company.getName(),
                SearchSuggestionType.COMPANY,
                company.getLogoUrl(),
                null
        );
    }

}