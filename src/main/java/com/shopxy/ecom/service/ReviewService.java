package com.shopxy.ecom.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.ReviewRepository;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.Review;
import com.shopxy.ecom.model.User;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public Review createReview(String name, String email, String description, Product product, User user,
            String ratings) {
        Review review = new Review();
        review.setDate(LocalDate.now());
        review.setEmail(email);
        review.setName(name);
        review.setProduct(product);
        review.setRating(ratings);
        review.setReviewDescription(description);
        review.setUser(user);
        reviewRepository.save(review);
        return review;
    }

    public List<Review> findAllReviewProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public String getAvgRating(Product product) {
        List<Review> reviews = findAllReviewProduct(product);
        int sumRating = 0;
        for (Review rev : reviews) {
            sumRating += Integer.parseInt(rev.getRating());
        }
        int avgRating = 0;
        if (!reviews.isEmpty()) {
            avgRating = sumRating / reviews.size();
        }
        return String.valueOf(avgRating);
    }

    public Map<Long, String> getProductReview(List<Product> productList) {
        Map<Long,String> avgRating=new HashMap<>();
        for (Product product : productList) {
            String avgRatingss = getAvgRating(product);
            avgRating.put(product.getId(), avgRatingss);
        }
        return avgRating;
    }

}
