package com.boot.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boot.smart.entity.User;
import com.boot.smart.helper.Message;
import com.boot.smart.service.UserService;

import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	 @Autowired 
	 private UserService userService;
	 
	 
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Sign up - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/handleSignup")
	public String handleSignup(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
			Model model) {

		if(!agreement)
		{
			System.out.println("User hasnt agreed to T&C");
			model.addAttribute("user", user);
			model.addAttribute("message", new Message("You must accept the Terms and Conditions", "alert-danger"));
			return "signup";
		}
		if(result.hasErrors())
		{
			model.addAttribute("user", user);
			return "signup";
		}
		try {
			user.setUserRole("ROLE_USER");
			user.setUserActive(true);
			user.setUserPic("default.png");
			user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
			User savedUser = this.userService.createUser(user);
			model.addAttribute("user", user);
			model.addAttribute("message", new Message("Successfully Registered!!", "alert-success"));
			System.out.println(savedUser);
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			model.addAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
			return "signup";
		}
		return "signup";
	}
	@GetMapping("/custom-login")
	public String customLogin(Model model)
	{
		System.out.println("Into custom login method");
		model.addAttribute("title","Custom Login Page");
		
		return "custom-login";
	}
}
