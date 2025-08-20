package com.prototype.dev_exchange.controller;


import com.prototype.dev_exchange.dto.ConnectionDTO;
import com.prototype.dev_exchange.service.ConnectionService;
import com.prototype.dev_exchange.status.ConnectionStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionService connectionService;
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/send")
    public ConnectionDTO sendRequest(@RequestParam Long requesterId, @RequestParam Long receiverId) {
        return connectionService.sendRequest(requesterId, receiverId);
    }

    @PutMapping("/{connectionId}/status")
    public ConnectionDTO updateStatus(@PathVariable Long connectionId, @RequestParam ConnectionStatus status) {
        return connectionService.updateStatus(connectionId, status);
    }

    @GetMapping("/{userId}")
    public List<ConnectionDTO> getConnections(@PathVariable Long userId) {
        return connectionService.getConnections(userId);
    }
}
