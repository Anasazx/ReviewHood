package tn.anasazx.tunirate.search.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.dto.CompanyResponse;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.company.service.CompanyService;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.search.dto.SearchResponseDTO;
import tn.anasazx.tunirate.search.service.SearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    @Override
    public SearchResponseDTO search(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return new SearchResponseDTO(List.of(), List.of());
        }

        List<ProductResponse> products = searchProduct(q);
        List<CompanyResponse> companies = searchCompany(q);

        return new SearchResponseDTO(products, companies);
    }




    public List<ProductResponse> searchProduct(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return List.of();
        }

        return productRepository
                .search(q)
                .stream()
                .map(ProductMapper::mapProductToResponse)
                .toList();
    }

    public List<CompanyResponse> searchCompany(String query) {

        String q = (query == null) ? "" : query.trim();

        if (q.isEmpty()) {
            return List.of();
        }

        return companyRepository.search(q)
                .stream()
                .map(CompanyMapper::toResponse)
                .toList();
    }

}
