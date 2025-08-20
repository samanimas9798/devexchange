package com.prototype.dev_exchange.repository;

import com.prototype.dev_exchange.entity.Connection;
import com.prototype.dev_exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByRequesterOrReceiver(User requester, User receiver);
}