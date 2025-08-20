package com.prototype.dev_exchange.controller;

import com.prototype.dev_exchange.dto.QuestionDTO;
import com.prototype.dev_exchange.entity.Question;
import com.prototype.dev_exchange.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:8080")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get all questions as DTOs
    @GetMapping
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions()
                .stream()
                .map(questionService::convertToDTO)
                .collect(Collectors.toList());
    }

    // Post a new question
    @PostMapping("/{userId}")
    public QuestionDTO postQuestion(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) { // changed String -> Object

        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        List<String> tags = (List<String>) payload.get("tags"); // new line

        Question question = questionService.postQuestion(userId, title, description, tags); // pass tags
        return questionService.convertToDTO(question);
    }
}
