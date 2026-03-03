package com.example.ecom.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.utility.AddressMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.ecom.model.Address;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.service.UserService;
import com.example.ecom.utility.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userRepository.findAll().stream()
                .map(UserMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
        log.debug("Fetched {} users", users.size());
        return users;
    }

    @Override
    @Transactional
    public void addUser(UserRequest userRequest) {
        log.info("Adding new user with email={}", userRequest.getEmail());
        userRepository.save(UserMapper.mapUserRequestToUser(userRequest));
        log.debug("User saved successfully: {}", userRequest);
    }

    @Override
    public Optional<UserResponse> getUser(Long id) {
        log.info("Fetching user with id={}", id);
        Optional<UserResponse> userResponse = userRepository.findById(id)
                .map(UserMapper::mapUserToUserResponse);
        if (userResponse.isPresent()) {
            log.debug("User found with id={}", id);
        } else {
            log.warn("User not found with id={}", id);
        }
        return userResponse;
    }

    @Override
    @Transactional
    public boolean updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with id={}", id);
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPhNo(userRequest.getPhNo());

            Address address = existingUser.getAddress();
            if (address == null) {
                log.debug("No address found for user id={}, creating new one", id);
                address = new Address();
                existingUser.setAddress(AddressMapper.mapAddressDTOToAddress(userRequest.getAddress()));
            }
            if (userRequest.getAddress() != null) {
                log.debug("Updating address for user id={}", id);
                address.setStreet(userRequest.getAddress().getStreet());
                address.setCity(userRequest.getAddress().getCity());
                address.setState(userRequest.getAddress().getState());
                address.setCountry(userRequest.getAddress().getCountry());
                address.setZipCode(userRequest.getAddress().getZipCode());
            }

            userRepository.save(existingUser);
            log.info("User updated successfully with id={}", id);
            return true;
        }).orElseGet(() -> {
            log.warn("User not found, update failed for id={}", id);
            return false;
        });
    }
}
