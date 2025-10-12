package com.example.ecom.service;

import java.util.List;
import java.util.Optional;

import com.example.ecom.model.User;

public interface UserService {

    List<User> getAllUsers();

    void addUser(User user);

    Optional<User> getUser(Long id);

    boolean updateUser(Long id, User user);
}