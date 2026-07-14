package tn.anasazx.tunirate.review.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.mapper.CommentMapper;
import tn.anasazx.tunirate.comment.repository.CommentRepository;
import tn.anasazx.tunirate.enums.ActorType;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;
import tn.anasazx.tunirate.review.dto.ProductReviewsResponse;
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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final CompanyMemberRepository companyMemberRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;


    @Override
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        return reviewRepository
                .findAll(pageable)
                .map(this::mapReview);
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        return this.mapReview(findReviewEntity(id));
    }


    @Override
    public  ProductReviewsResponse getReviewsByProductId(Long productId, Pageable pageable) {

        ReviewResponse myReview = null;

        if (SecurityUtils.getCurrentUserIdOrNull() != null){
            Long currentUserId = SecurityUtils.getCurrentUserId();

            myReview = reviewRepository
                    .findByUserIdAndProductId(currentUserId, productId)
                    .map(this::mapReview)
                    .orElse(null);
        }


        Page<ReviewResponse> reviews = reviewRepository
                .findByProductId(productId, pageable)
                .map(this::mapReview);

        return new ProductReviewsResponse(myReview, reviews);
    }

    @Override
    public Page<MinimizedReviewResponse> getMyReviews(Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return reviewRepository.findReviewsByUserId(currentUserId, pageable)
                .map(ReviewMapper::toMinimizedResponse);
    }


    @Override
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(this::mapReview);
    }

    @Override
    public List<ReviewResponse> getMyCompanyReviews() {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        Optional<CompanyMember> membershipOpt = companyMemberRepository.findFirstByUserId(currentUserId);

        if (membershipOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        Long companyId = membershipOpt.get().getCompany().getId();

        return reviewRepository.findByProductCompanyId(companyId).stream()
                .map(this::mapReview)
                .toList();

    }

    @Override
    public ReviewResponse createReview(ReviewRequest request) {

        //This gets the users id from the token
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //This checks if the product exists or not; it takes the product id from the request in the params
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        /*
        From our business model each user has the right for one review per product, so
        this methode throws an exception if a user has already made a review
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

        return this.mapReview(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(Long id, ReviewRequest request) {

        Review review = findReviewEntity(id);

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User user = findUserEntity(currentUserId);

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

        return this.mapReview(reviewRepository.save(review));
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

    private ReviewResponse mapReview(Review review) {

        Long commentsCount = commentRepository.countByReview_Id(review.getId());

        CommentResponse previewComment = getPreviewComment(review);

        return ReviewMapper.toResponse(
                review,
                commentsCount,
                previewComment
        );
    }

    private CommentResponse getPreviewComment(Review review){

        Long companyId = review.getProduct().getCompany().getId();

        return commentRepository
                .findFirstByReview_IdAndActor_TypeAndActor_IdOrderByCreatedAtDesc(review.getId(), ActorType.COMPANY, companyId)
                .map(CommentMapper::toResponse)
                .orElse(null);
    }


}

