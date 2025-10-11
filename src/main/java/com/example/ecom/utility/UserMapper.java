package com.example.ecom.utility;

import com.example.ecom.model.User;
import com.example.ecom.model.Address;
import com.example.ecom.dto.UserRequest;
import com.example.ecom.dto.UserResponse;
import com.example.ecom.dto.AddressDTO;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId() != null ? user.getId().toString() : null);
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhNo(user.getPhNo());
        response.setUserRole(user.getUserRole());
        if (user.getAddress() != null) {
            response.setAddressDto(toAddressDTO(user.getAddress()));
        }
        return response;
    }

    public static User toUser(UserRequest request) {
        if (request == null) return null;
        User user = new User();
        if (request.getId() != null) {
            try {
                user.setId(Long.parseLong(request.getId()));
            } catch (NumberFormatException ignored) {}
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhNo(request.getPhNo());
        if (request.getAddressDto() != null) {
            user.setAddress(toAddress(request.getAddressDto()));
        }
        // UserRole is not present in UserRequest, set default if needed
        return user;
    }

    public static AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setZipCode(address.getZipCode());
        return dto;
    }

    public static Address toAddress(AddressDTO dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());
        return address;
    }
}

