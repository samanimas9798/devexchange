package com.prototype.dev_exchange.controller;


import com.prototype.dev_exchange.dto.ReplyDTO;
import com.prototype.dev_exchange.entity.Reply;
import com.prototype.dev_exchange.service.ReplyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/replies")
@CrossOrigin(origins = "http://localhost:8080")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/{questionId}/{userId}")
    public ReplyDTO postReply(
            @PathVariable Long questionId,
            @PathVariable Long userId,
            @RequestBody Map<String, String> payload) {

        String content = payload.get("content");
        Reply reply = replyService.postReply(questionId, userId, content);
        return new ReplyDTO(reply.getId(), reply.getContent(), reply.getUser().getUsername(), reply.getRepliedAt());
    }

    @GetMapping("/{questionId}")
    public List<ReplyDTO> getReplies(@PathVariable Long questionId) {
        return replyService.getRepliesForQuestion(questionId);
    }
}
