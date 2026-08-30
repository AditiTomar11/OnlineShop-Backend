package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.FeedbackRequest;
import com.onlineShop.OnlineShop.entity.Feedback;
import com.onlineShop.OnlineShop.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Public — koi bhi feedback submit kar sake, login zaroori nahi
    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackRequest request) {
        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .rating(request.getRating())
                .build();

        feedbackRepository.save(feedback);
        return ResponseEntity.ok("Feedback submitted successfully");
    }
}