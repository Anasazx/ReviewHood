package tn.anasazx.tunirate.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.search.dto.SearchResponseDTO;
import tn.anasazx.tunirate.search.dto.SearchSuggestionDTO;
import tn.anasazx.tunirate.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public SearchResponseDTO search(@RequestParam String query, Pageable page) {
        return searchService.search(query, page);
    }

    @GetMapping("/products")
    public Page<ProductResponse> searchProducts(@RequestParam String query, Pageable pageable) {
        return searchService.searchProducts(query, pageable);
    }

    @GetMapping("/companies")
    public Page<CompanyResponse> searchCompanies(@RequestParam String query, Pageable pageable) {
        return searchService.searchCompanies(query, pageable);
    }

    @GetMapping("/suggestions")
    public List<SearchSuggestionDTO> suggestions(@RequestParam String query) {
        return searchService.suggestions(query);
    }

}
