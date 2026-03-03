package com.example.ecom.utility;

import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.model.User;

public class UserMapper {
    public static UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhNo(user.getPhNo());
        userResponse.setUserRole(user.getUserRole());
        if (user.getAddress() != null) {
            userResponse.setAddress(AddressMapper.mapAddressToAddressDTO(user.getAddress()));
        }
        return userResponse;
    }

    public static User mapUserRequestToUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhNo(userRequest.getPhNo());
        if (userRequest.getAddress() != null) {
            user.setAddress(AddressMapper.mapAddressDTOToAddress(userRequest.getAddress()));
        }
        return user;
    }
}
