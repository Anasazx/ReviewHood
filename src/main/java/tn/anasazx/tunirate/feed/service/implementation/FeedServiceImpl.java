package tn.anasazx.tunirate.feed.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.mapper.CompanyMapper;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.feed.dto.FeedResponse;
import tn.anasazx.tunirate.feed.service.FeedService;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.mapper.ProductMapper;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.mapper.ReviewMapper;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
import tn.anasazx.tunirate.subcategory.mapper.SubcategoryMapper;
import tn.anasazx.tunirate.subcategory.repository.SubcategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CompanyRepository companyRepository;


    private final ProductMapper productMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final CompanyMapper companyMapper;

    private static final int PRODUCT_LIMIT = 24;
    private static final int REVIEW_LIMIT = 18;
    private static final int COMPANY_LIMIT = 18;

    @Override
    public FeedResponse getFeed() {

        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, PRODUCT_LIMIT));

        List<Review> reviews = reviewRepository.findByContentIsNotNullOrderByCreatedAtDesc(PageRequest.of(0, REVIEW_LIMIT));

        // this is just for now, we return the subcategories of the cat beauty as our categories
        // List<Subcategory> subcategories = subcategoryRepository.findAll();
        List<Subcategory> subcategories = subcategoryRepository.findByCategoryName("Beauty");

        List<Company> companies = companyRepository.findAll(PageRequest.of(0, COMPANY_LIMIT)).getContent();

        return new FeedResponse(
                products.stream().map(productMapper::toResponse).toList(),
                reviews.stream().map(ReviewMapper::toMinimizedResponse).toList(),
                subcategories.stream().map(subcategoryMapper::toResponse).toList(),
                companies.stream().map(companyMapper::toResponse).toList()
        );
    }


}
