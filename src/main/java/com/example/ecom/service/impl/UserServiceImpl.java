package com.example.ecom.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ecom.utility.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.model.User;
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
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public void addUser(@RequestBody UserRequest userRequest) {
        User user = UserMapper.toUser(userRequest);
        userRepository.save(user);
    }

    @Override
    public Optional<UserResponse> getUser(Long id) {
        return userRepository.findById(id).map(UserMapper::toUserResponse);
    }

    @Override
    public boolean updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id).map(existingUser -> {
            User updatedUser = UserMapper.toUser(userRequest);
            // Only update fields from request, keep unchanged fields
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhNo(updatedUser.getPhNo());
            if (updatedUser.getAddress() != null) {
                if (existingUser.getAddress() == null) {
                    existingUser.setAddress(updatedUser.getAddress());
                } else {
                    existingUser.getAddress().setStreet(updatedUser.getAddress().getStreet());
                    existingUser.getAddress().setCity(updatedUser.getAddress().getCity());
                    existingUser.getAddress().setState(updatedUser.getAddress().getState());
                    existingUser.getAddress().setCountry(updatedUser.getAddress().getCountry());
                    existingUser.getAddress().setZipCode(updatedUser.getAddress().getZipCode());
                }
            }
            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }
}