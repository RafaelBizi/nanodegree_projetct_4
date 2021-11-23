package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		ResponseEntity<Item> itemResponseEntity = ResponseEntity.of(itemRepository.findById(id));
		HttpStatus status = HttpStatus.valueOf(itemResponseEntity.getStatusCodeValue());
		if (status.is2xxSuccessful()){
			LOGGER.info("m=getItemById, Item ID {} found properly!", id);
		} else if (status.is4xxClientError()){
			LOGGER.error("m=getItemById, Item ID {} not found!", id);
		}
		return itemResponseEntity;
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		ResponseEntity<?> responseEntity = items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
		HttpStatus status = HttpStatus.valueOf(responseEntity.getStatusCodeValue());
		if (status.is2xxSuccessful()){
			LOGGER.info("m=getItemsByName, Item name {} found properly!", name);
		} else if (status.is4xxClientError()){
			LOGGER.error("m=getItemsByName, Item name {} not found!", name);
		}
		return (ResponseEntity<List<Item>>) responseEntity;
	}
	
}
