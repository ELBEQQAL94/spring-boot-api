package com.learning.app.ws.services.impl;

import com.learning.app.ws.dto.UserDTO;
import com.learning.app.ws.entities.UserEntity;
import com.learning.app.ws.repositories.UserRepository;
import com.learning.app.ws.services.UserService;
import com.learning.app.ws.utils.GenerateId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    GenerateId generateId;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GenerateId generateId, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.generateId = generateId;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        UserEntity isUserExists = userRepository.findByEmail(user.getEmail());
        if(isUserExists != null) throw  new RuntimeException("User already exists");
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String generateUserId = generateId.generateId(20);
        userEntity.setUserId(generateUserId);
        UserEntity newUser = userRepository.save(userEntity);
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(newUser, userDto);
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
