package com.boot.smart.service;

import java.util.List;

import com.boot.smart.entity.Contact;
import com.boot.smart.entity.User;

public interface ContactService {

	List<Contact> searchContact(String query, User user);

}
