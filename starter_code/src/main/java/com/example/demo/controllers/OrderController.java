package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> orderSubmit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			LOGGER.error("m=orderSubmit, Username {} not found!", username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		LOGGER.info("m=orderSubmit, Order was submitted properly!");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			LOGGER.error("m=getOrdersForUser, Username {} not found!", username);
			return ResponseEntity.notFound().build();
		}
		LOGGER.info("m=getOrdersForUser, Orders found properly!", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
