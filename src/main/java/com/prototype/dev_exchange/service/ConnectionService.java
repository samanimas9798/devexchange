package com.prototype.dev_exchange.service;

import com.prototype.dev_exchange.dto.ConnectionDTO;
import com.prototype.dev_exchange.entity.Connection;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.repository.ConnectionRepository;
import com.prototype.dev_exchange.repository.UserRepository;
import com.prototype.dev_exchange.status.ConnectionStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRepository connectionRepository, UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    private ConnectionDTO toDTO(Connection connection) {
        ConnectionDTO dto = new ConnectionDTO();
        dto.setId(connection.getId());
        dto.setRequesterId(connection.getRequester().getId());
        dto.setRequesterUsername(connection.getRequester().getUsername());
        dto.setReceiverId(connection.getReceiver().getId());
        dto.setReceiverUsername(connection.getReceiver().getUsername());
        dto.setStatus(connection.getStatus());
        dto.setGoogleMeetLink(connection.getGoogleMeetLink());
        dto.setCreatedAt(connection.getCreatedAt());
        return dto;

    }

    public ConnectionDTO sendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId).orElseThrow(()->new RuntimeException("Requester Not Found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(()->new RuntimeException("Receiver Not Found"));
        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);

        return toDTO(connectionRepository.save(connection));
    }

    public ConnectionDTO updateStatus(Long connectionId, ConnectionStatus status) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection Not Found"));

        connection.setStatus(status);

        // If connection is accepted, generate a Google Meet link
        if (status == ConnectionStatus.ACCEPTED && (connection.getGoogleMeetLink() == null || connection.getGoogleMeetLink().isEmpty())) {
            // Simple random meeting link for now
            String meetLink = "https://meet.google.com/" + java.util.UUID.randomUUID().toString().substring(0, 10);
            connection.setGoogleMeetLink(meetLink);
        }

        return toDTO(connectionRepository.save(connection));
    }

    public List<ConnectionDTO> getConnections(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));

        return connectionRepository.findByRequesterOrReceiver(user,user).stream().map(this::toDTO).collect(Collectors.toList());

    }


}
