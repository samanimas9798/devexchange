package com.prototype.dev_exchange.repository;

import com.prototype.dev_exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByUsername(String username);
Optional<User> findByEmail(String email);
}


