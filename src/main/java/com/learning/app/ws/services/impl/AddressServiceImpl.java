package com.learning.app.ws.services.impl;

import com.learning.app.ws.dto.AddressDTO;
import com.learning.app.ws.entities.AddressEntity;
import com.learning.app.ws.entities.UserEntity;
import com.learning.app.ws.repositories.AddressRepository;
import com.learning.app.ws.repositories.UserRepository;
import com.learning.app.ws.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.TypeToken;

@Service
public class AddressServiceImpl implements AddressService {

    UserRepository userRepository;
    AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl( AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDTO> getAddress(String email, int page, int limit) {
        if(page > 0) page -= 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        UserEntity authUser = userRepository.findByEmail(email);
        Page<AddressEntity> addresses;
        if(authUser.getAdmin()) {
            addresses = addressRepository.findAllAddresses(pageableRequest);
        } else {
            addresses = addressRepository.findAllAddressesByEmail(pageableRequest, authUser.getUserId());
        }
        List<AddressEntity> addressEntity = addresses.getContent();
        Type listType = new TypeToken<List<AddressDTO>>() {}.getType();
        List<AddressDTO> addressesDTO = new ModelMapper().map(addressEntity, listType);
        return addressesDTO;
    }

    public String createAddress() {
        return "create adddress";
    }

    public String updateAddress() {
        return "update address";
    }

    public String deleteAddress() {
        return "deleteAddress";
    }
}
