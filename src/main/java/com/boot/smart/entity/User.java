package com.boot.smart.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class User {

	// Private attributes
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int userId;
	
	@Size(min=3,max=15, message="Username must be of 3-15 characters.")
	private String userName;
	
	@Column(unique = true)
	@Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="Invalid Email!")
	private String userEmail;
	
	@Size(min=8, message="Password must contain atleast 8 characters.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$]).+$",
	        message = "Password must contain at least 1 uppercase letter, 1 digit, and 1 special character (@, #, $)")
	private String userPassword;
	
	@Size(max=500, message="Description cannot exceed 500 characters.")
	private String userDescription;

	private String userPic;
	private String userRole;
	private boolean userActive;
	//1:n mapping
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="user")
	@JsonManagedReference
	private List<Contact> contactList = new ArrayList<>();

	public User() {
	}

	public User(int userId, String userName, String userEmail, String userPassword, String userPic,
			String userDescription, String userRole, boolean userActive, List<Contact> contactList) {
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userPic = userPic;
		this.userDescription = userDescription;
		this.userRole = userRole;
		this.userActive = userActive;
		this.contactList = contactList;
	}

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isUserActive() {
		return userActive;
	}

	public void setUserActive(boolean userActive) {
		this.userActive = userActive;
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", userPassword="
				+ userPassword + ", userPic=" + userPic + ", userDescription=" + userDescription + ", userRole="
				+ userRole + ", userActive=" + userActive + ", contactList=" + contactList + "]";
	}
}
