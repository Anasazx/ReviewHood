package tn.anasazx.tunirate.review.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream().map(reviewMapper::toResponse).toList();
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        return reviewMapper.toResponse(findReview(id));
    }


    @Override
    public Page<ReviewResponse> getReviewsByProductId(
            Long productId,
            Pageable pageable
    ) {
        return reviewRepository
                .findByProductId(productId, pageable)
                .map(reviewMapper::toResponse);
    }
    /*
    @Override
    public List<ReviewResponse> getAllReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId).stream().map(reviewMapper::toResponse).toList();
    }
    */

    @Override
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(reviewMapper::toResponse);
    }

    @Override
    public ReviewResponse createReview(ReviewRequest request) {

        //This get the users id from the token
        User user = findUser(SecurityUtils.getCurrentUserId());
        System.out.println("This is the result of findUser:(Highlighting the id now)" + user.getId());

        //This check if the product exists or not; it takes the product id from the request in the params
        //TODO: We need to throw an exception when no product found with this id
        Product product = findProduct(request.productId());
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

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        Review review = findReview(id);
        User user = findUser(SecurityUtils.getCurrentUserId());
        Product product = findProduct(request.productId());

        reviewRepository.findByUserIdAndProductId(SecurityUtils.getCurrentUserId(), request.productId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Review already exists for this user and product");
                });

        review.setRating(request.rating());
        review.setContent(request.content());
        review.setUser(user);
        review.setProduct(product);

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.delete(findReview(id));
    }

    private Review findReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}

