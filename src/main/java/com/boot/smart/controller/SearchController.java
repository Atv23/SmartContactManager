package com.boot.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boot.smart.entity.Contact;
import com.boot.smart.entity.User;
import com.boot.smart.service.ContactService;
import com.boot.smart.service.UserService;

@RestController
public class SearchController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ContactService contactService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query, Principal principal)
	{
		System.out.println(query);
		User user = this.userService.getLoggedInUser(principal.getName());
		List<Contact> contactList = this.contactService.searchContact(query,user);
		return new ResponseEntity(contactList, HttpStatus.OK);
	}
}
