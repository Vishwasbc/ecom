package com.example.ecom.utility;

import com.example.ecom.dto.AddressDTO;
import com.example.ecom.model.Address;

public class AddressMapper {
    public static AddressDTO mapAddressToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    public static Address mapAddressDTOToAddress(AddressDTO addressDTO) {
        Address addressObj = new Address();
        addressObj.setStreet(addressDTO.getStreet());
        addressObj.setCity(addressDTO.getCity());
        addressObj.setState(addressDTO.getState());
        addressObj.setCountry(addressDTO.getCountry());
        addressObj.setZipCode(addressDTO.getZipCode());
        return addressObj;
    }
}
