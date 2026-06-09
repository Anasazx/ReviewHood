package tn.anasazx.tunirate.product.service.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.categories.subcategory.repository.SubcategoryRepository;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.product.dto.ProductDetailsResponse;
import tn.anasazx.tunirate.product.dto.ProductRequest;
import tn.anasazx.tunirate.product.dto.ProductResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.review.service.ReviewService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final SubcategoryRepository subcategoryRepository;

    private final ReviewService reviewService;


    //This methode returns the product with his details such as reviews...
    //There is a methode that returns only the basic info of a product called getProductById
    @Override
    public ProductDetailsResponse getProductDetails(Long id) {

        Product product = findProduct(id);

        double avgRating = reviewService.getAverageRatingByProductId(id);

        long reviewsCount = reviewService.countByProductId(id);

        return ProductMapper.mapProductToDetailsResponse(product, avgRating, reviewsCount);

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::mapProductToResponse)
                .toList();
    }

    //This only return the basic info of a product without reviews...,
    //there is another methode that return detailed product called getProductDetails
    @Override
    public ProductResponse getProductById(Long id) {
        return ProductMapper.mapProductToResponse(findProduct(id));
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Company company = findCompany(request.companyId());

        Subcategory subcategory = resolveSubcategory(request.subcategoryId());

        Product product = ProductMapper.toProduct(request, company, subcategory);

        Product savedProduct = productRepository.save(product);

        return ProductMapper.mapProductToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = findProduct(id);

        Company company = findCompany(request.companyId());

        Subcategory subcategory = resolveSubcategory(request.subcategoryId());

        ProductMapper.updateProduct(product, request, company, subcategory);

        return ProductMapper.mapProductToResponse(productRepository.save(product));
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Override
    public Company findCompany(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }


    private Subcategory resolveSubcategory(Long id) {
        if (id == null) return null;
        return findSubcategory(id);
    }

    @Override
    public Subcategory findSubcategory(Long id) {
        return subcategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcategory not found"));
    }

    @Override
    public List<ProductResponse> search(String query) {

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

    @Override
    public List<ProductResponse> getProductsByCompanyId(Long companyId) {
        findCompany(companyId);
        return productRepository.findByCompanyId(companyId)
                .stream()
                .map(ProductMapper::mapProductToResponse)
                .toList();
    }


}
