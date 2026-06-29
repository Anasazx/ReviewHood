package tn.anasazx.tunirate.product.service.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.subcategory.repository.SubcategoryRepository;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.product.service.ProductService;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;


    //This methode returns the product with his details such as reviews...
    //There is a methode that returns only the basic info of a product called getProductById
    @Override
    public ProductDetailsResponse getProductDetailsById(Long ProductId) {
        Product product = findProductById(ProductId);

        Long companyId = product.getCompany().getId();

        double avgRating = Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(companyId)).orElse(0.0);

        long reviewsCount = reviewRepository.countByProductId(ProductId);

        return ProductMapper.mapProductToDetailsResponse(product, avgRating, reviewsCount);
    }

    @Override
    public AdminProductDetailsResponse getProductDetailsByIdAsAdmin(Long ProductId) {

        Product product = findProductById(ProductId);

        Long companyId = product.getCompany().getId();

        double avgRating = Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(companyId)).orElse(0.0);

        long reviewsCount = reviewRepository.countByProductId(ProductId);

        return ProductMapper.mapProductToAdminDetailsResponse(product, avgRating, reviewsCount);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return findAllProducts().stream()
                .map(ProductMapper::mapProductToResponse)
                .toList();
    }

    //This only return the basic info of a product without reviews...,
    //there is another methode that return detailed product called getProductDetails
    @Override
    public ProductResponse getProductById(Long productId) {

        Product product = findProductById(productId);

        return ProductMapper.mapProductToResponse(product);

    }

    @Override
    public AdminProductResponse createProductAsAdmin(AdminProductRequest request) {

        Company company = findCompanyById(request.companyId());

        Subcategory subcategory = findSubcategoryById(request.subcategoryId());

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User currentUser = findUserByUserId(currentUserId);

        Product product =  new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCompany(company);
        product.setSubcategory(subcategory);
        product.setCreatedBy(currentUser);

        return ProductMapper.mapProductToAdminResponse(productRepository.save(product));
    }

    @Override
    public CompanyProductResponse createProductAsCompany(CompanyProductRequest request) {

        Long currentUserId =  SecurityUtils.getCurrentUserId();

        CompanyMember membership = findMembershipByUserId(currentUserId);
        User currentUser = membership.getUser();
        Company company = membership.getCompany();

        Subcategory subcategory = findSubcategoryById(request.subcategoryId());



        Product product =  new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCompany(company);
        product.setSubcategory(subcategory);
        product.setCreatedBy(currentUser);
        if (request.status() == ProductStatus.DRAFT) product.setStatus(ProductStatus.DRAFT);

        return ProductMapper.mapProductToCompanyResponse(productRepository.save(product));

    }

    //This methode is only for the admin
    //TODO: we need an update product for the company
    @Override
    public AdminProductResponse updateProductAsAdmin(Long productId, AdminProductRequest request) {

        Product product = findProductByIdAsAdmin(productId);

        Company company = findCompanyById(request.companyId());

        Subcategory subcategory = findSubcategoryById(request.subcategoryId());

        product.setName(request.name());
        product.setDescription(request.description());
        product.setSubcategory(subcategory);
        product.setCompany(company);

        return ProductMapper.mapProductToAdminResponse(productRepository.save(product));

    }


    //TODO: Change the hard delete to something else
    @Override
    public void deleteProductAsAdmin(Long id) {
        Product product = findProductByIdAsAdmin(id);
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getProductsByCompanyId(Long companyId) {
        return findProductsByCompanyId(companyId)
                .stream()
                .map(ProductMapper::mapProductToResponse)
                .toList();
    }

    @Override
    public List<CompanyProductResponse> getProductsAsCompany() {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        CompanyMember membership = findMembershipByUserId(currentUserId);

        Long companyId = membership.getCompany().getId();

        return findProductsByCompanyIdAsCompany(companyId)
                .stream()
                .map(ProductMapper::mapProductToCompanyResponse)
                .toList();
    }

    @Override
    public List<AdminProductResponse> getProductsAsAdmin() {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        CompanyMember membership = findMembershipByUserId(currentUserId);

        Long companyId = membership.getCompany().getId();

        return findAllProductsAsAdmin()
                .stream()
                .map(ProductMapper::mapProductToAdminResponse)
                .toList();
    }

    // -- HELPERS --

    User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)
        );
    }

    CompanyMember findMembershipByUserId(Long userId) {
        return companyMemberRepository.findFirstByUserId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not belong to any company!")
        );
    }

    Subcategory findSubcategoryById(Long subcategoryId){
        return  subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Company findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Product findProductByIdAsAdmin(Long productId){
        return productRepository.findByStatusNotAndId(ProductStatus.DRAFT ,productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Product findProductByIdAsCompany(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Product findProductById(Long productId){
        return productRepository.findByStatusAndId(ProductStatus.PUBLISHED, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    List<Product> findProductsByCompanyIdAsCompany(Long companyId){
        return productRepository.findByCompanyId(companyId);
    }

    List<Product> findProductsByCompanyId(Long companyId){
        return productRepository.findByCompanyIdAndStatus(companyId,  ProductStatus.PUBLISHED);
    }

    List<Product> findAllProducts(){
        return productRepository.findAllByStatusOrderByCreatedAtAsc(ProductStatus.PUBLISHED);
    }

    List<Product> findAllProductsAsAdmin(){
        return productRepository.findAllByStatusNot(ProductStatus.DRAFT);
    }

}
