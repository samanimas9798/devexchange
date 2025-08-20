package com.prototype.dev_exchange.repository;

import com.prototype.dev_exchange.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
