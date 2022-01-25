package com.learning.app.ws.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String city;
    private String country;
    private String street;
    private String postal_code;
    private String type;
}
