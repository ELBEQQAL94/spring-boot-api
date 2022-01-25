package com.learning.app.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long id;
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postal_code;
    private String type;
    private UserDTO user;
}
