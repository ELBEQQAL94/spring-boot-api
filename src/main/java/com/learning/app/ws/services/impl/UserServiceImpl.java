package com.learning.app.ws.services.impl;

import com.learning.app.ws.dto.AddressDTO;
import com.learning.app.ws.dto.UserDTO;
import com.learning.app.ws.entities.AddressEntity;
import com.learning.app.ws.entities.UserEntity;
import com.learning.app.ws.repositories.UserRepository;
import com.learning.app.ws.services.UserService;
import com.learning.app.ws.utils.GenerateStringId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    GenerateStringId generateStringId;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GenerateStringId generateStringId, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.generateStringId = generateStringId;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        UserEntity isUserExists = userRepository.findByEmail(user.getEmail());
        if(isUserExists != null) throw  new RuntimeException("User already exists");
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        for(int i = 0; i < user.getAddresses().size(); i++) {
            AddressEntity address = userEntity.getAddresses().get(i);
            address.setUser(userEntity);
            String generateAddressId = generateStringId.generateId(20);
            address.setAddressId(generateAddressId);
            userEntity.getAddresses().set(i, address);
        }
        String generateContactId = generateStringId.generateId(20);
        userEntity.getContact().setUser(userEntity);
        userEntity.getContact().setContactId(generateContactId);
        String generateUserId = generateStringId.generateId(20);
        userEntity.setUserId(generateUserId);
        UserEntity newUser = userRepository.save(userEntity);
        UserDTO userDto = modelMapper.map(newUser, UserDTO.class);
        return userDto;
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity isUserExists = userRepository.findByEmail(email);
        if(isUserExists == null) throw  new UsernameNotFoundException(email);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(isUserExists, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        ModelMapper modelMapper = new ModelMapper();
        if(user == null) throw  new UsernameNotFoundException(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO user, String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw  new UsernameNotFoundException(userId);
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updatedUser = userRepository.save(userEntity);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(updatedUser, userDTO);
        return userDTO;
    }

    @Override
    public String deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw  new UsernameNotFoundException(userId);
        userRepository.delete(userEntity);
        return "User deleted";
    }

    @Override
    public List<UserDTO> getUsers(int page, int limit, String filter, Boolean status) {
        if(page > 0) page -= 1;
        List<UserDTO> usersDTO = new ArrayList<>();
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> users;
        if(filter.isEmpty()) {
            users = userRepository.findAllUsers(pageableRequest);
        } else {
            users = userRepository.findUsersByFilter(pageableRequest, filter, status);
        }
        List<UserEntity> usersEntity = users.getContent();
        for (UserEntity userEntity:usersEntity) {
            ModelMapper modelMapper = new ModelMapper();
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
