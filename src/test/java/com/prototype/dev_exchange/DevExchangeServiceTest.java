package com.prototype.dev_exchange;

import com.prototype.dev_exchange.dto.UserDTO;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.entity.Profile;
import com.prototype.dev_exchange.repository.UserRepository;
import com.prototype.dev_exchange.repository.ProfileRepository;
import com.prototype.dev_exchange.service.UserService;
import com.prototype.dev_exchange.service.ProfileService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DevExchangeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private ProfileService profileService;

    // UserService Test
    @Test
    void testCreateUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Sam");
        userDTO.setEmail("sam@example.com");
        userDTO.setPassword("123");
        userDTO.setTechToLearn("Python");
        userDTO.setTechToShare("Java");


        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("Sam");
        savedUser.setEmail("sam@example.com");
        savedUser.setPassword("123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Profile savedProfile = new Profile();
        savedProfile.setUser(savedUser);
        savedProfile.setTechToLearn("Python");
        savedProfile.setTechToShare("Java");

        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);


        UserDTO created = userService.createUser(userDTO);


        assertEquals("Sam", created.getUsername());
        assertEquals("Python", created.getTechToLearn());
        assertEquals("Java", created.getTechToShare());
    }

    // ProfileService: sendConnectionRequest
    @Test
    void testSendConnectionRequest() {
        Profile sender = new Profile();
        sender.setId(1L);
        sender.setPendingRequestsSent(new ArrayList<>());

        Profile receiver = new Profile();
        receiver.setId(2L);
        receiver.setPendingRequestsReceived(new ArrayList<>());

        when(profileRepository.findByUser_Id(1L)).thenReturn(Optional.of(sender));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(profileRepository.save(any(Profile.class))).thenReturn(sender).thenReturn(receiver);

        profileService.sendConnectionRequest(1L, 2L);

        assertTrue(sender.getPendingRequestsSent().contains(2L));
        assertTrue(receiver.getPendingRequestsReceived().contains(1L));
    }

    // ProfileService: acceptConnectionRequest
    @Test
    void testAcceptConnectionRequest() {
        Profile receiver = new Profile();
        receiver.setId(2L);
        List<Long> received = new ArrayList<>();
        received.add(1L);
        receiver.setPendingRequestsReceived(received);
        receiver.setConnections(new ArrayList<>());

        Profile sender = new Profile();
        sender.setId(1L);
        sender.setPendingRequestsSent(new ArrayList<>());
        sender.setConnections(new ArrayList<>());
        sender.getPendingRequestsSent().add(2L);

        when(profileRepository.findByUser_Id(2L)).thenReturn(Optional.of(receiver));
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(profileRepository.save(any(Profile.class))).thenReturn(receiver).thenReturn(sender);

        profileService.acceptConnectionRequest(2L, 1L);

        assertTrue(receiver.getConnections().contains(1L));
        assertTrue(sender.getConnections().contains(2L));
        assertFalse(receiver.getPendingRequestsReceived().contains(1L));
        assertFalse(sender.getPendingRequestsSent().contains(2L));
    }

    // ProfileService: declineConnectionRequest
    @Test
    void testDeclineConnectionRequest() {
        Profile receiver = new Profile();
        receiver.setId(2L);
        List<Long> received = new ArrayList<>();
        received.add(1L);
        receiver.setPendingRequestsReceived(received);

        Profile sender = new Profile();
        sender.setId(1L);
        sender.setPendingRequestsSent(new ArrayList<>());
        sender.getPendingRequestsSent().add(2L);

        when(profileRepository.findByUser_Id(2L)).thenReturn(Optional.of(receiver));
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(profileRepository.save(any(Profile.class))).thenReturn(receiver).thenReturn(sender);

        profileService.declineConnectionRequest(2L, 1L);

        assertFalse(receiver.getPendingRequestsReceived().contains(1L));
        assertFalse(sender.getPendingRequestsSent().contains(2L));
    }

}
