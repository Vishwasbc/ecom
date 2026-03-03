package com.example.ecom.service.impl;

import com.example.ecom.dto.AddressDTO;
import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.model.Address;
import com.example.ecom.model.User;
import com.example.ecom.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setPhNo("1234567890");
        Address address = new Address();
        address.setStreet("Main");
        address.setCity("City");
        sampleUser.setAddress(address);
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<UserResponse> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
    }

    @Test
    void addUser_shouldSave() {
        UserRequest req = new UserRequest();
        req.setFirstName("Jane");
        req.setLastName("Smith");
        req.setEmail("jane@example.com");
        req.setPhNo("0987654321");
        AddressDTO addr = new AddressDTO();
        addr.setStreet("St");
        req.setAddress(addr);

        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.addUser(req);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUser_whenExists_shouldReturn() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<UserResponse> opt = userService.getUser(1L);

        assertTrue(opt.isPresent());
        assertEquals("John", opt.get().getFirstName());
    }

    @Test
    void updateUser_whenExists_shouldReturnTrue() {
        UserRequest req = new UserRequest();
        req.setFirstName("Johnny");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        boolean res = userService.updateUser(1L, req);

        assertTrue(res);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUser_whenNotFound_shouldReturnEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponse> opt = userService.getUser(99L);

        assertFalse(opt.isPresent());
    }

    @Test
    void updateUser_whenNotFound_shouldReturnFalse() {
        UserRequest req = new UserRequest();
        req.setFirstName("Nope");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        boolean res = userService.updateUser(99L, req);

        assertFalse(res);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addUser_withNullAddress_shouldSave() {
        UserRequest req = new UserRequest();
        req.setFirstName("Anna");
        req.setLastName("Bell");
        req.setEmail("anna@example.com");
        req.setPhNo("555");
        req.setAddress(null);

        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.addUser(req);

        verify(userRepository, times(1)).save(any(User.class));
    }
}
