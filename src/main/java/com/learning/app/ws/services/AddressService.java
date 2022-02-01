package com.learning.app.ws.services;

import com.learning.app.ws.dto.AddressDTO;
import com.learning.app.ws.entities.AddressEntity;
import com.learning.app.ws.entities.UserEntity;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddress(String email, int page, int limit);
    String createAddress();
    String updateAddress();
    String deleteAddress();
}
