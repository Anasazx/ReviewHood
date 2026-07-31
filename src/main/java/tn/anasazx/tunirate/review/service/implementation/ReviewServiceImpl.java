package tn.anasazx.tunirate.review.service.implementation;

import jakarta.transaction.Transactional;
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
import tn.anasazx.tunirate.like.commentLike.repository.CommentLikeRepository;
import tn.anasazx.tunirate.like.reviewLike.repository.ReviewLikeRepository;
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
    private final ReviewLikeRepository reviewLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        return reviewRepository
                .findAll(pageable)
                .map(r -> mapReview(r, null));
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        return this.mapReview(findReviewEntity(id), null);
    }

    @Transactional
    @Override
    public ReviewResponse createReview(ReviewRequest request, Long currentUserId) {

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        reviewRepository
                .findByUserIdAndProductId(currentUserId, request.productId())
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this user and product");
                });

        Review review = new Review();
        review.setRating(request.rating());
        review.setContent(request.content());
        review.setUser(user);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        productRepository.recalculateReviewStats(product.getId());

        return mapReview(savedReview, currentUserId);
    }

    @Transactional
    @Override
    public ReviewResponse updateReview(Long id, ReviewRequest request, Long currentUserId) {

        Review review = findReviewEntity(id);

        if (!review.getUser().getId().equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        review.setRating(request.rating());
        review.setContent(request.content());

        Review savedReview = reviewRepository.save(review);

        productRepository.recalculateReviewStats(review.getProduct().getId());

        return mapReview(savedReview, currentUserId);
    }

    @Transactional
    @Override
    public void deleteReview(Long id, Long currentUserId) {

        Review review = findReviewEntity(id);

        if (!review.getUser().getId().equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Long productId = review.getProduct().getId();

        reviewRepository.delete(review);

        productRepository.recalculateReviewStats(productId);
    }

    @Override
    public ProductReviewsResponse getReviewsByProductId(Long productId, Pageable pageable, Long currentUserId) {

        ReviewResponse myReview = null;

        if (currentUserId != null){

            myReview = reviewRepository
                    .findByUserIdAndProductId(currentUserId, productId)
                    .map(r -> mapReview(r, currentUserId))
                    .orElse(null);
        }


        Page<ReviewResponse> reviews = reviewRepository
                .findByProductId(productId, pageable)
                .map(r -> mapReview(r, currentUserId));

        return new ProductReviewsResponse(myReview, reviews);
    }

    @Override
    public Page<MinimizedReviewResponse> getMyReviews(Pageable pageable, Long currentUserId) {
        return reviewRepository.findReviewsByUserId(currentUserId, pageable)
                .map(ReviewMapper::toMinimizedResponse);
    }

    @Override
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(r -> mapReview(r, null));
    }

    @Override
    public List<ReviewResponse> getMyCompanyReviews(Long currentUserId) {

        Optional<CompanyMember> membershipOpt = companyMemberRepository.findFirstByUserId(currentUserId);

        if (membershipOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        Long companyId = membershipOpt.get().getCompany().getId();

        return reviewRepository.findByProductCompanyId(companyId).stream()
                .map(r -> mapReview(r, currentUserId))
                .toList();
    }

    private Review findReviewEntity(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    private ReviewResponse mapReview(Review review, Long currentUserId) {

        Long commentsCount = commentRepository.countByReview_Id(review.getId());

        CommentResponse previewComment = getPreviewComment(review, currentUserId);

        boolean liked = false;

        if (currentUserId != null) liked = reviewLikeRepository.existsByReview_IdAndUser_Id(review.getId(), currentUserId);

        return ReviewMapper.toResponse(
                review,
                commentsCount,
                previewComment,
                liked
        );
    }

    private CommentResponse getPreviewComment(Review review, Long currentUserId) {

        Long companyId = review.getProduct().getCompany().getId();

        return commentRepository
                .findFirstByReview_IdAndActor_TypeAndActor_IdOrderByCreatedAtDesc(
                        review.getId(),
                        ActorType.COMPANY,
                        companyId
                )
                .map(comment -> {
                    boolean liked = currentUserId != null &&
                            commentLikeRepository.existsByComment_IdAndUser_Id(
                                    comment.getId(),
                                    currentUserId
                            );

                    return CommentMapper.toResponse(comment, liked);
                })
                .orElse(null);
    }

}

