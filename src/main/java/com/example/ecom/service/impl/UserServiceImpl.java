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
        return userRepository.findAll().stream()
                .map(UserMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void addUser(UserRequest userRequest) {
        userRepository.save(UserMapper.mapUserRequestToUser(userRequest));
    }

    @Override
    public Optional<UserResponse> getUser(Long id) {
        return userRepository.findById(id).map(UserMapper::mapUserToUserResponse);
    }

    @Override
    @Transactional
    public boolean updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPhNo(userRequest.getPhNo());
            Address address = existingUser.getAddress();
            if (address == null) {
                address = new Address();
                existingUser.setAddress(AddressMapper.mapAddressDTOToAddress(userRequest.getAddress()));
            }
            if (userRequest.getAddress() != null) {
                address.setStreet(userRequest.getAddress().getStreet());
                address.setCity(userRequest.getAddress().getCity());
                address.setState(userRequest.getAddress().getState());
                address.setCountry(userRequest.getAddress().getCountry());
                address.setZipCode(userRequest.getAddress().getZipCode());
            }
            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }
}