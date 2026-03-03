package com.example.ecom.controller;

import java.util.List;

import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userService.getAllUsers();
        log.debug("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to add new user with email={}", userRequest.getEmail());
        userService.addUser(userRequest);
        log.info("User added successfully with email={}", userRequest.getEmail());
        return ResponseEntity.ok("User Added Successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("Fetching user with id={}", id);
        return userService.getUser(id)
                .map(user -> {
                    log.info("User found with id={}", id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found with id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        log.info("Updating user with id={}", id);
        boolean updated = userService.updateUser(id, userRequest);
        if (updated) {
            log.info("User updated successfully with id={}", id);
            return ResponseEntity.ok("User Updated successfully");
        } else {
            log.warn("User not found, update failed for id={}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
