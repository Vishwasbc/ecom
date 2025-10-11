package com.example.ecom.service;

import java.util.List;
import java.util.Optional;

import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;

public interface UserService {

    List<UserResponse> getAllUsers();

    void addUser(UserRequest userRequest);

    Optional<UserResponse> getUser(Long id);

    boolean updateUser(Long id, UserRequest userRequest);
}