package tn.anasazx.tunirate.review.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.dto.ReviewRequest;
import tn.anasazx.tunirate.review.dto.ReviewResponse;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.mapper.ReviewMapper;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.review.service.ReviewService;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final CompanyMemberService companyMemberService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewMapper::toResponse)
                .toList();
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        return ReviewMapper.toResponse(findReviewEntity(id));
    }


    @Override
    public Page<ReviewResponse> getReviewsByProductId(Long productId, Pageable pageable) {

        Long userId = SecurityUtils.getCurrentUserId();

        Optional<ReviewResponse> userReview =
                reviewRepository.findByUserIdAndProductId(userId, productId)
                        .map(ReviewMapper::toResponse);

        List<ReviewResponse> rawReviews =
                reviewRepository.findByProductId(productId, pageable)
                        .map(ReviewMapper::toResponse)
                        .getContent();

        List<ReviewResponse> merged = userReview
                .map(ur -> Stream.concat(
                        Stream.of(ur),
                        rawReviews.stream().filter(r -> !r.id().equals(ur.id()))
                ).toList())
                .orElse(rawReviews);

        return new PageImpl<>(merged, pageable, merged.size());
    }


    //This methode perform a specific query to return the average rating for a specific product
    public double getAverageRatingByProductId(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    //This methode returns the number of review for a specific product
    @Override
    public long countByProductId(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Override
    public Optional<ReviewResponse> getUserReviewForProduct(Long userId, Long productId) {
        return reviewRepository
                .findByUserIdAndProductId(userId, productId)
                .map(ReviewMapper::toResponse);
    }


    @Override
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewMapper::toResponse);
    }

    @Override
    public List<ReviewResponse> getMyCompanyReviews() {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyMemberResponse membership = companyMemberService.getCompanyByUserId(userId);

        if (membership == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        Long companyId = membership.company().id();

        return reviewRepository.findByProductCompanyId(companyId).stream()
                .map(ReviewMapper::toResponse)
                .toList();

    }

    @Override
    public ReviewResponse createReview(ReviewRequest request) {

        //This get the users id from the token
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //This check if the product exists or not; it takes the product id from the request in the params
        //TODO: We need to throw an exception when no product found with this id
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        /*
        From our business model each user have the right for one review per product so
        this methode throw an exception if a user have already made a review
        */
        reviewRepository
                .findByUserIdAndProductId(SecurityUtils.getCurrentUserId(), request.productId())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this user and product");
                });


        Review review = new Review();
        review.setRating(request.rating());
        review.setContent(request.content());
        review.setUser(user);
        review.setProduct(product);

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(Long id, ReviewRequest request) {

        Review review = findReviewEntity(id);

        Long userId = SecurityUtils.getCurrentUserId();

        User user = findUserEntity(userId);

        Product product = findProductEntity(request.productId());

        reviewRepository.findByUserIdAndProductId(SecurityUtils.getCurrentUserId(), request.productId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this user and product");
                });

        review.setRating(request.rating());
        review.setContent(request.content());
        review.setUser(user);
        review.setProduct(product);

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.delete(findReviewEntity(id));
    }

    private Review findReviewEntity(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    private User findUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Product findProductEntity(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}

