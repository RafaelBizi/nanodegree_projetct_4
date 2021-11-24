package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findUserById(@PathVariable Long id) {
		ResponseEntity<User> userResponseEntity = ResponseEntity.of(userRepository.findById(id));
		HttpStatus status = HttpStatus.valueOf(userResponseEntity.getStatusCodeValue());
		if (status.is2xxSuccessful()){
			LOGGER.info("m=findUserById, User ID {} found properly!", id);
		} else if (status.is4xxClientError()){
			LOGGER.error("m=findUserById, User ID {} not found!", id);
		}
		return userResponseEntity;
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		ResponseEntity<?> responseEntity = user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
		HttpStatus status = HttpStatus.valueOf(responseEntity.getStatusCodeValue());
		if (status.is2xxSuccessful()){
			LOGGER.info("m=findByUserName, User {} found properly!", username);
		} else if (status.is4xxClientError()){
			LOGGER.error("m=findByUserName, User {} not found!", username);
		}
		return (ResponseEntity<User>) responseEntity;
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			LOGGER.error("createUser, Password is different than the confirmed password");
			return ResponseEntity.badRequest().header("header1","Password is different than the confirmed password!").build();
		}

		if (createUserRequest.getConfirmPassword().equals("") ||
		    createUserRequest.getConfirmPassword() == null ||
			createUserRequest.getConfirmPassword().length() == 0 ||
			createUserRequest.getConfirmPassword().length() < 8) {
			LOGGER.error("createUser, Password can't be less than 8 chars or null!");
			return ResponseEntity.badRequest().header("header2","Avoid empty or less than 8 chars password!").build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
		LOGGER.info("m=createUser, User {} created properly!", user.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
