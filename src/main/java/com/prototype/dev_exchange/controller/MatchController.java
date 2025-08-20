package com.prototype.dev_exchange.controller;

import com.prototype.dev_exchange.dto.ProfileDTO;
import com.prototype.dev_exchange.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // GET endpoint to find matches for a given user
    @GetMapping("/{userId}")
    public List<ProfileDTO> findMatches(@PathVariable Long userId) {
        return matchService.findMatches(userId);
    }
}