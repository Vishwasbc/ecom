package com.example.orders.dto;

import com.example.orders.dto.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phNo;
    private UserRole userRole;
    private AddressDTO address;
}
