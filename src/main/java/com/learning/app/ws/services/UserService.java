package com.learning.app.ws.services;

import com.learning.app.ws.dto.UserDTO;
import com.learning.app.ws.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUser(String email);
    UserDTO getUserByUserId(String userId);
    UserDTO updateUser(UserDTO userDTO, String userId);
    String deleteUser(String userId);
}
