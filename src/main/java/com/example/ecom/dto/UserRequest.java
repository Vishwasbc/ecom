package com.example.ecom.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phNo;
    private AddressDTO addressDto;
}