package tn.anasazx.tunirate.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public void updateProductScore(Long productId) {

        Product product = productRepository.findById(productId)

                .orElseThrow();

        List<Review> reviews = reviewRepository.findAllByProductId(productId);

        if (reviews.isEmpty()) {




            return;

        }

        System.out.println("Product: " + productId);
        System.out.println("Reviews: " + reviews.size());

        for (Review review : reviews) {
            System.out.println(review.getRating());
        }

        // Bayesian formula comes here

    }

}