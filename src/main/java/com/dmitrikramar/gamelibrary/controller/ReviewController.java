package com.dmitrikramar.gamelibrary.controller;

import com.dmitrikramar.gamelibrary.entity.Review;
import com.dmitrikramar.gamelibrary.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for handling review-related API requests.

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.getById(id)
                .map(existingReview -> {
                    existingReview.setRating(review.getRating());
                    existingReview.setComment(review.getComment());
                    Review updatedReview = reviewService.save(existingReview);
                    return ResponseEntity.ok(updatedReview);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (reviewService.existsById(id)) {
            reviewService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
