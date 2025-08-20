package com.prototype.dev_exchange.repository;

import com.prototype.dev_exchange.entity.Question;
import com.prototype.dev_exchange.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List <Reply> findByQuestion(Question question);
}
