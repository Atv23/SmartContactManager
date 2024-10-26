package com.boot.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boot.smart.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	@Query("Select u From User u Where u.userEmail= :email")
	public User getUserByUsername(@Param("email") String email);
}
