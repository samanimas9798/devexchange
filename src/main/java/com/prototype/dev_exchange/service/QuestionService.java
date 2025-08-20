package com.prototype.dev_exchange.service;

import com.prototype.dev_exchange.dto.QuestionDTO;
import com.prototype.dev_exchange.entity.Question;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.repository.QuestionRepository;
import com.prototype.dev_exchange.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public QuestionDTO convertToDTO(Question question) {
        String username = (question.getUser() != null) ? question.getUser().getUsername() : "Unknown";
        return new QuestionDTO(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getPostedAt(),
                username, question.getTags());
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question postQuestion(Long userId, String title, String description, List<String> tags) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Question question = new Question(title, description, user);
        question.setTags(tags);
        question.setPostedAt(LocalDateTime.now());

        return questionRepository.save(question);
    }

}
