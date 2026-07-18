package tn.anasazx.tunirate.product.service.implementation;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.fileStorage.service.FileStorageService;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.dto.*;
import tn.anasazx.tunirate.product.entity.ProductImage;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FileStorageService fileStorageService;

    //This methode returns the product with his details such as reviews...
    //There is a methode that returns only the basic info of a product called getProductById
    @Override
    public ProductDetailsResponse getProductDetailsById(Long ProductId) {
        Product product = findProductByIdStatusPublished(ProductId);

        Long companyId = product.getCompany().getId();

        double avgRating = Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(companyId)).orElse(0.0);

        long reviewsCount = reviewRepository.countByProductId(ProductId);

        return ProductMapper.toDetailsResponse(product, avgRating, reviewsCount);
    }

    @Override
    public AdminProductDetailsResponse getProductDetailsByIdAsAdmin(Long ProductId) {

        Product product = findProductById(ProductId);

        Long companyId = product.getCompany().getId();

        double avgRating = Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(companyId)).orElse(0.0);

        long reviewsCount = reviewRepository.countByProductId(ProductId);

        return ProductMapper.toAdminDetailsResponse(product, avgRating, reviewsCount);
    }

    public Page<ProductResponse> getAllProducts(
            Long categoryId,
            Long subcategoryId,
            Pageable pageable
    ) {

        if (subcategoryId != null) {
            return productRepository
                    .findAllByStatusAndSubcategoryId(
                            ProductStatus.PUBLISHED,
                            subcategoryId,
                            pageable
                    )
                    .map(ProductMapper::toResponse);

        }
        else if (categoryId != null) {
            return productRepository
                    .findAllByStatusAndSubcategoryCategoryId(
                            ProductStatus.PUBLISHED,
                            categoryId,
                            pageable
                    )
                    .map(ProductMapper::toResponse);
        }

        return productRepository
                .findAllByStatus(ProductStatus.PUBLISHED, pageable)
                .map(ProductMapper::toResponse);
    }

    @Override
    public ProductResponse getProductById(Long productId) {

        Product product = findProductByIdStatusPublished(productId);

        return ProductMapper.toResponse(product);

    }

    @Override
    @Transactional
    public AdminProductResponse createProductAsAdmin(AdminProductRequest request, List<MultipartFile> images) {

        Company company = findCompanyById(request.companyId());

        Subcategory subcategory = request.subcategoryId() == null ? null : findSubcategoryById(request.subcategoryId());

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User currentUser = findUserByUserId(currentUserId);

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCompany(company);
        product.setSubcategory(subcategory);
        product.setCreatedBy(currentUser);

        product.setStatus(request.status() != null ? request.status() : ProductStatus.PENDING_REVIEW);

        // Save first to generate product ID
        productRepository.save(product);

        if (images != null && !images.isEmpty()) {

            for (int i = 0; i < images.size(); i++) {

                MultipartFile file = images.get(i);

                String fileName = fileStorageService.saveFile(file);

                ProductImage image = new ProductImage();

                image.setUrl(fileName);
                image.setMain(i == 0);

                product.addImage(image);
            }
        }

        productRepository.save(product);

        return ProductMapper.toAdminResponse(product);
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

        return ProductMapper.toCompanyResponse(productRepository.save(product));

    }

    //TODO: we need an update product for the company
    @Override
    @Transactional
    public AdminProductResponse updateProductAsAdmin(Long productId, AdminProductRequest request, List<MultipartFile> images) {

        Product product = findProductByIdAsAdmin(productId);

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() ->
                        new RuntimeException("Company not found")
                );

        Subcategory subcategory = null;

        if (request.subcategoryId() != null) {
            subcategory = subcategoryRepository.findById(request.subcategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCompany(company);
        product.setSubcategory(subcategory);

        if (request.status() != null) {
            product.setStatus(request.status());
        }

        /*
         * Add new images
         */
        if (images != null && !images.isEmpty()) {

            boolean hasMainImage = product.getImages()
                    .stream()
                    .anyMatch(ProductImage::isMain);

            for (MultipartFile file : images) {

                String fileName = fileStorageService.saveFile(file);

                ProductImage image = new ProductImage();

                image.setProduct(product);
                image.setUrl(fileName);

                // if a product has no main image, the first new image becomes the main
                image.setMain(!hasMainImage);

                if (!hasMainImage) {
                    hasMainImage = true;
                }

                product.getImages().add(image);
            }
        }

        productRepository.save(product);

        return ProductMapper.toAdminResponse(product);
    }


    @Override
    public void updateProductStatusAsAdmin(Long productId, ProductStatus productStatus) {
        Product product = findProductById(productId);
        product.setStatus(productStatus);
        productRepository.save(product);
    }

    @Override
    public Page<ProductResponse> getProductsByCompanyId(Long companyId, Pageable pageable) {
        return productRepository.findByCompanyIdAndStatus(companyId,  ProductStatus.PUBLISHED, pageable)
                .map(ProductMapper::toResponse);
    }

    @Override
    public List<CompanyProductResponse> getProductsAsCompany() {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        CompanyMember membership = findMembershipByUserId(currentUserId);

        Long companyId = membership.getCompany().getId();

        return findProductsByCompanyIdAsCompany(companyId)
                .stream()
                .map(ProductMapper::toCompanyResponse)
                .toList();
    }

    @Override
    public List<AdminProductResponse> getProductsAsAdmin() {
        return findAllProductsAsAdmin()
                .stream()
                .map(ProductMapper::toAdminResponse)
                .toList();
    }

    @Override
    public CompanyProductResponse getProductByIdAsCompany(Long productId) {

        Product product = findProductByIdStatusPublished(productId);

        return ProductMapper.toCompanyResponse(product);

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

    Product findProductByIdStatusPublished(Long productId){
        return productRepository.findByStatusAndId(ProductStatus.PUBLISHED, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Product findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    List<Product> findProductsByCompanyIdAsCompany(Long companyId){
        return productRepository.findByCompanyId(companyId);
    }

    List<Product> findAllProductsAsAdmin(){
        return productRepository.findAllByStatusNot(ProductStatus.DRAFT);
    }

}
