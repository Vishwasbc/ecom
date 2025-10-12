package com.example.ecom.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecom.model.User;
import com.example.ecom.model.Address;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean updateUser(Long id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            // Only update fields from request-like User object, keep unchanged fields
            if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
            if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getPhNo() != null) existingUser.setPhNo(user.getPhNo());
            if (user.getUserRole() != null) existingUser.setUserRole(user.getUserRole());

            Address incomingAddress = user.getAddress();
            if (incomingAddress != null) {
                if (existingUser.getAddress() == null) {
                    existingUser.setAddress(incomingAddress);
                } else {
                    if (incomingAddress.getStreet() != null) existingUser.getAddress().setStreet(incomingAddress.getStreet());
                    if (incomingAddress.getCity() != null) existingUser.getAddress().setCity(incomingAddress.getCity());
                    if (incomingAddress.getState() != null) existingUser.getAddress().setState(incomingAddress.getState());
                    if (incomingAddress.getCountry() != null) existingUser.getAddress().setCountry(incomingAddress.getCountry());
                    if (incomingAddress.getZipCode() != null) existingUser.getAddress().setZipCode(incomingAddress.getZipCode());
                }
            }

            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }
}