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

    public static Address mapAddressDTOToAddress(AddressDTO address) {
        Address addr = new Address();
        addr.setStreet(address.getStreet());
        addr.setCity(address.getCity());
        addr.setState(address.getState());
        addr.setCountry(address.getCountry());
        addr.setZipCode(address.getZipCode());
        return addr;
    }
}
