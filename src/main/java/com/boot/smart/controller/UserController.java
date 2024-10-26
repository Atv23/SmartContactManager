package com.boot.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.smart.dao.ContactRepository;
import com.boot.smart.dao.UserRepository;
import com.boot.smart.entity.Contact;
import com.boot.smart.entity.User;
import com.boot.smart.helper.Message;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to all handlers
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userEmail = principal.getName();
		System.out.println("Email of logged in user: " + userEmail);
		// get User object by useremail
		User userLoggedIn = this.userRepository.getUserByUsername(userEmail);
		model.addAttribute("user", userLoggedIn);
	}

	// user dashboard
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("title", "Dashboard");
		return "normal/user_dashboard";
	}

	// add contact
	@GetMapping("/add-contact")
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/user_addContact";
	}

	// handle add contact form
	@PostMapping("/processContact")
	public String handleAddContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			Principal principal, Model model, @RequestParam("uploadPic") MultipartFile file) {
		if (result.hasErrors()) {
			// Log all errors for debugging
			result.getAllErrors().forEach(error -> {
				System.out.println("Error: " + error.getDefaultMessage());
			});
			model.addAttribute("contact", contact);
			model.addAttribute("message", new Message("Oops!! You may have filled something wrong", "alert-danger"));
			return "normal/user_addContact";
		}
		try {
			System.out.println(contact);
			String userEmail = principal.getName();
			User userLoggedIn = this.userRepository.getUserByUsername(userEmail);
			// save user in contact tabel
			contact.setUser(userLoggedIn);
			// processing and uploading file...
			if (file.isEmpty())
				System.out.println("Image not uploaded");
			else {
				// storing filename in contact obj
				contact.setContactPic(file.getOriginalFilename());
				// uploading file to project folder
				File uploadedImg = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(uploadedImg.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image Uploaded");
			}
			// save contact in user's contact list
			userLoggedIn.getContactList().add(contact);
			// save contact in DB through user
			this.userRepository.save(userLoggedIn);
			model.addAttribute("contact", new Contact());
			model.addAttribute("message", new Message("Contact Successfully Added!!", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("contact", contact);
			model.addAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
			return "normal/user_addContact";
		}
		return "normal/user_addContact";
	}

	// display all contacts
	// per page = 5
	// current page = 0
	@GetMapping("/view-contact/{pageNo}")
	public String viewContact(@PathVariable("pageNo") Integer pageNo, Model model, Principal principal) {
		System.out.println("Into View Contacts handle method");
		model.addAttribute("title", "View Contacts");
		String userEmail = principal.getName();
		User user = this.userRepository.getUserByUsername(userEmail);
		int contactsPerPage = 5;
		// Pageable is parent interface of PageRequest, used for pagination, has 2
		// arguments: currentPer-page & contactsPer-page
		Pageable pageable = PageRequest.of(pageNo, contactsPerPage);
		Page<Contact> contacts = this.contactRepository.findContactByUserId(user.getUserId(), pageable);
		System.out.println(contacts);

		// Check if there are any contacts and adjust totalPage accordingly
		int totalPages = contacts.getTotalPages();
		// Check if the requested pageNo is valid
		if (pageNo >= totalPages && totalPages > 0) {
			pageNo = totalPages - 1; // Reset to the last valid page if requested page is out of bounds
		} else if (totalPages == 0) {
			pageNo = 0; // Reset to the first page if no contacts are available
		}
		model.addAttribute("contactList", contacts);
		model.addAttribute("itemPerPage", contactsPerPage);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPage", totalPages > 0 ? totalPages : 1); // Set to 1 if totalPages is 0
		System.out.println("Total pages:" + contacts.getTotalPages());
		return "normal/user_viewContact";
	}

	// display detailed contact view
	@GetMapping("/contact/{id}")
	public String detailContact(@PathVariable("id") Integer id, Model model, Principal principal) {
		System.out.println("Showing detail contact view of Contact ID: " + id);
		model.addAttribute("title", "Contact Details");
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		// validation for user to only see his contacts
		String userEmail = principal.getName();
		User validUser = this.userRepository.getUserByUsername(userEmail);
		if (validUser.getUserId() == contact.getUser().getUserId())
			model.addAttribute("contact", contact);
		return "normal/user_contactDetails";
	}

	// delete contact
	@GetMapping("/deleteContact/{id}")
	public String handleDelete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,
			Principal principal) throws IOException {
		System.out.println("Into delete contact handler");
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		// validation if user is deleting only his contacts
		String userEmail = principal.getName();
		User userLoggedin = this.userRepository.getUserByUsername(userEmail);
		if (userLoggedin.getUserId() == contact.getUser().getUserId()) {
			redirectAttributes.addFlashAttribute("message",
					new Message("Contact Deleted Successfully!!", "alert-success"));
			this.contactRepository.delete(contact);
			//deleting image from target folder
			File deleteImg = new ClassPathResource("static/img").getFile();
			File deleteFile = new File(deleteImg,contact.getContactPic());
			deleteFile.delete();
		} else {
			System.out.println("Attempting to delete someone else's contact");
			redirectAttributes.addFlashAttribute("message",
					new Message("You cannot delete someone else's contact", "alert-danger"));
		}

		return "redirect:/user/view-contact/0";
	}

	// update contact
	@PostMapping("/updateContact/{id}")
	public String updateContact(@PathVariable("id") Integer id, Model model, Principal principal) {
		System.out.println("Into update contact handler");
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		model.addAttribute("title", "Update Contact");
		model.addAttribute("contact", contact);
		return "normal/user_updateContact";
	}

	// update contact handler
	@PostMapping("/processUpdate")
	public String handleUpdate(@Valid @ModelAttribute Contact contact, BindingResult result, Principal principal,
			Model model, @RequestParam("uploadPic") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			// Log all errors for debugging
			result.getAllErrors().forEach(error -> {
				System.out.println("Error: " + error.getDefaultMessage());
			});
			model.addAttribute("contact", contact);
			model.addAttribute("message", new Message("Oops!! You may have filled something wrong", "alert-danger"));
			return "normal/user_updateContact";
		}
		try {
			Contact oldContact = this.contactRepository.findById(contact.getContactId()).get();
			if (!file.isEmpty()) {
				// rewrite file
				//deleting old file
				File deleteImg = new ClassPathResource("static/img").getFile();
				File deleteFile = new File(deleteImg,oldContact.getContactPic());
				deleteFile.delete();
				// uploading new file to project folder
				File uploadedImg = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(uploadedImg.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				// updating filename in contact obj
				contact.setContactPic(file.getOriginalFilename());
			}
			else {
				contact.setContactPic(oldContact.getContactPic());
			}
			// user will be null if we do not set it manually
			String userEmail = principal.getName();
			User user = this.userRepository.getUserByUsername(userEmail);
			contact.setUser(user);
			this.contactRepository.save(contact);
			redirectAttributes.addFlashAttribute("message", new Message("Contact Successfully Updated!!", "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("contact", contact);
			model.addAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
			return "normal/user_updateContact";
		}
		return "redirect:/user/view-contact/0";
	}
	
	//view profile
	@GetMapping("/profile")
	public String showProgile(Model model, Principal principal)
	{
		String userEmail = principal.getName();
		User userLoggedin = this.userRepository.getUserByUsername(userEmail);
		int contactCount= this.contactRepository.countByUserId(userLoggedin.getUserId());
		model.addAttribute("title","Profile");
		model.addAttribute("contactCount", contactCount);
		return "normal/user_viewProfile";
	}
}
