package com.boot.smart.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {

	// Private attributes
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int contactId;
	
	@NotBlank(message="Contact Name cannot be empty")
	private String contactName;
	
	@Size(max=1000, message="Description cannot exceed 1000 characters")
	private String contactDescription;
	private String contactPic;
	private String contactNickname;
	private String contactWork;
	
	@Column(unique = true)
	@Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" ,message="Invalid Email!")
	private String contactEmail;
	
	@NotBlank(message="Contact Number cannot be empty")
	@Pattern(regexp="^[1-9][0-9]{9}$", message="Phone number must be 10 digits and cannot start with 0")
	private String contactNumber;
	//n:1 mapping
	@ManyToOne
	@JsonBackReference
	private User user;
	
	// Default constructor
	public Contact() {
	}

	// Parameterized constructor
	public Contact(int contactId, String contactName, String contactDescription, String contactPic,
			String contactNickname, String contactWork, String contactEmail, String contactNumber) {
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactDescription = contactDescription;
		this.contactPic = contactPic;
		this.contactNickname = contactNickname;
		this.contactWork = contactWork;
		this.contactEmail = contactEmail;
		this.contactNumber = contactNumber;
	}

	// Getters and Setters
	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactDescription() {
		return contactDescription;
	}

	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}

	public String getContactPic() {
		return contactPic;
	}

	public void setContactPic(String contactPic) {
		this.contactPic = contactPic;
	}

	public String getContactNickname() {
		return contactNickname;
	}

	public void setContactNickname(String contactNickname) {
		this.contactNickname = contactNickname;
	}

	public String getContactWork() {
		return contactWork;
	}

	public void setContactWork(String contactWork) {
		this.contactWork = contactWork;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [contactId=" + contactId + ", contactName=" + contactName + ", contactDescription="
				+ contactDescription + ", contactPic=" + contactPic + ", contactNickname=" + contactNickname
				+ ", contactWork=" + contactWork + ", contactEmail=" + contactEmail + ", contactNumber=" + contactNumber
				+  "]";
	}

}
