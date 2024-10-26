package com.boot.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boot.smart.entity.Contact;
import com.boot.smart.entity.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
		
	//Pageable interface for pagination, has 2 arguments: currentPer-page & contactsPer-page
	@Query("from Contact c where c.user.userId=:id")
	public Page<Contact> findContactByUserId(@Param("id")int id, Pageable pageable);
	
	@Query("SELECT COUNT(c) FROM Contact c WHERE c.user.userId = :userId")
    public int countByUserId(@Param("userId") int userId);	
	
	public List<Contact> findByContactNameContainingAndUser(String keyword, User user);
}
