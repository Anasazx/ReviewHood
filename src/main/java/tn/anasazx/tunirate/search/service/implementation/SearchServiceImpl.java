package tn.anasazx.tunirate.search.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.search.dto.SearchResponseDTO;
import tn.anasazx.tunirate.search.dto.SearchSuggestionDTO;
import tn.anasazx.tunirate.search.mapper.SearchMapper;
import tn.anasazx.tunirate.search.service.SearchService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final ProductMapper productMapper;
    private final CompanyMapper companyMapper;
    private final SearchMapper searchMapper;

    @Override
    public SearchResponseDTO search(String query, Pageable page) {

        String q = query == null ? "" : query.trim();

        if (q.isEmpty()) {
            return new SearchResponseDTO(
                    Page.empty(page),
                    Page.empty(page)
            );
        }

        Page<ProductResponse> products = searchProduct(q, page);
        Page<CompanyResponse> companies = searchCompany(q, page);

        return new SearchResponseDTO(products, companies);
    }

    @Override
    public Page<ProductResponse> searchProducts(String query, Pageable page) {
        String q = query == null ? "" : query.trim();

        if (q.isEmpty()) {
            return Page.empty(page);
        }

        return productRepository
                .search(q, page)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<CompanyResponse> searchCompanies(String query, Pageable page) {
        String q = query == null ? "" : query.trim();

        if (q.isEmpty()) {
            return Page.empty(page);
        }

        return companyRepository
                .search(q, page)
                .map(companyMapper::toResponse);
    }

    @Override
    public List<SearchSuggestionDTO> suggestions(String query) {

        String q = query == null ? "" : query.trim();

        if (q.length() < 2) {
            return List.of();
        }

        List<Product> products =
                productRepository.findSuggestions(q, PageRequest.of(0, 7));

        List<Company> companies =
                companyRepository.findSuggestions(q, PageRequest.of(0, 7));

        List<SearchSuggestionDTO> result = new ArrayList<>(7);

        // Up to 5 products
        products.stream()
                .limit(5)
                .map(searchMapper::toResponse)
                .forEach(result::add);

        // Up to 2 companies
        companies.stream()
                .limit(2)
                .map(searchMapper::toResponse)
                .forEach(result::add);

        // Fill remaining slots with products
        if (result.size() < 7) {
            products.stream()
                    .skip(5)
                    .limit(7 - result.size())
                    .map(searchMapper::toResponse)
                    .forEach(result::add);
        }

        // Fill remaining slots with companies
        if (result.size() < 7) {
            companies.stream()
                    .skip(2)
                    .limit(7 - result.size())
                    .map(searchMapper::toResponse)
                    .forEach(result::add);
        }

        return result;
    }

    public Page<ProductResponse> searchProduct(String query, Pageable page) {

        String q = query == null ? "" : query.trim();

        if (q.isEmpty()) {
            return Page.empty(page);
        }

        return productRepository
                .search(q, page)
                .map(productMapper::toResponse);
    }

    public Page<CompanyResponse> searchCompany(String query, Pageable page) {

        String q = query == null ? "" : query.trim();

        if (q.isEmpty()) {
            return Page.empty(page);
        }

        return companyRepository
                .search(q, page)
                .map(companyMapper::toResponse);
    }

}
