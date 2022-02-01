package com.learning.app.ws.controllers;

import com.learning.app.ws.dto.AddressDTO;
import com.learning.app.ws.requests.AddressRequest;
import com.learning.app.ws.requests.UserRequest;
import com.learning.app.ws.responses.AddressResponse;
import com.learning.app.ws.services.AddressService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AddressResponse>> getAddresses(
            Principal principal,
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "2") int limit
    ) {
        List<AddressDTO> addresses = addressService.getAddress(principal.getName(), page, limit);
        Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
        List<AddressResponse> addressesResponse = new ModelMapper().map(addresses, listType);
        return new ResponseEntity<List<AddressResponse>>(addressesResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String storeAddress(@RequestBody @Validated AddressRequest request) {
        return addressService.createAddress();
    }

    @PutMapping(path = "/{addressId}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String updateAddress(@PathVariable String addressId) {
        return addressService.updateAddress();
    }

    @DeleteMapping(path = "/{addressId}")
    public String deleteAddress(@PathVariable String addressId) {
        return addressService.deleteAddress();
    }
}
