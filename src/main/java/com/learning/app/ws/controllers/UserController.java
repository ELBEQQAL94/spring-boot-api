package com.learning.app.ws.controllers;

import com.learning.app.ws.dto.UserDTO;
import com.learning.app.ws.exceptions.UserException;
import com.learning.app.ws.requests.UserRequest;
import com.learning.app.ws.responses.ErrorMessages;
import com.learning.app.ws.responses.UserResponse;
import com.learning.app.ws.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserResponse> getUsers(
			@RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit,
			@RequestParam(value = "filter", defaultValue = "") String filter,
			@RequestParam(value = "status", defaultValue = "false") Boolean status
	) {
		List<UserResponse> usersResponse = new ArrayList<>();
		List<UserDTO> users = userService.getUsers(page, limit, filter, status);
		for(UserDTO userDTO: users) {
			ModelMapper modelMapper  = new ModelMapper();
			UserResponse user = modelMapper.map(userDTO, UserResponse.class);
			BeanUtils.copyProperties(userDTO, user);
			usersResponse.add(user);
		}
		return usersResponse;
	}

	@GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(userDTO, userResponse);
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserResponse> createUser(@RequestBody @Validated UserRequest request) throws Exception {
		if(request.getFirstName().isEmpty()) throw new UserException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDTO = modelMapper.map(request, UserDTO.class);
		UserDTO createUser = userService.createUser(userDTO);
		UserResponse userResponse = modelMapper.map(createUser, UserResponse.class);
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
