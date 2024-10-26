package com.boot.smart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.smart.dao.ContactRepository;
import com.boot.smart.entity.Contact;
import com.boot.smart.entity.User;

@Service
public class ContactServiceImpl implements ContactService{
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Override
	public List<Contact> searchContact(String query, User user) {
		return this.contactRepository.findByContactNameContainingAndUser(query, user);
	}

}
