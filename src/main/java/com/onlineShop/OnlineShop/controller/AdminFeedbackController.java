package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.entity.Feedback;
import com.onlineShop.OnlineShop.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackRepository feedbackRepository;

    public AdminFeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackRepository.findAllByOrderByCreatedAtDesc());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
        return ResponseEntity.ok("Feedback deleted");
    }
}