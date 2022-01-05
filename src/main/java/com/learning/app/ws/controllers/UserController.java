package com.learning.app.ws.controllers;

import com.learning.app.ws.dto.UserDTO;
import com.learning.app.ws.requests.UserRequest;
import com.learning.app.ws.responses.UserResponse;
import com.learning.app.ws.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(userDTO, userResponse);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(request, userDTO);
		UserDTO createUser = userService.createUser(userDTO);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(createUser, userResponse);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{userId}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserRequest request) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(request, userDTO);
		UserDTO updateUser = userService.updateUser(userDTO, userId);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(updateUser, userResponse);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
