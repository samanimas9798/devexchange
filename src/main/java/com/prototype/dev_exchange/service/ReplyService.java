package com.prototype.dev_exchange.service;


import com.prototype.dev_exchange.dto.ReplyDTO;
import com.prototype.dev_exchange.entity.Question;
import com.prototype.dev_exchange.entity.Reply;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.repository.QuestionRepository;
import com.prototype.dev_exchange.repository.ReplyRepository;
import com.prototype.dev_exchange.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public ReplyService(ReplyRepository replyRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public Reply postReply(Long questionId, Long userId, String content) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Reply reply = new Reply(content, question, user);
        return replyRepository.save(reply);
    }

    public List<ReplyDTO> getRepliesForQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        List<Reply> replies = replyRepository.findByQuestion(question);

        return replies.stream()
                .map(r -> new ReplyDTO(r.getId(), r.getContent(), r.getUser().getUsername(), r.getRepliedAt()))
                .collect(Collectors.toList());
    }

}
