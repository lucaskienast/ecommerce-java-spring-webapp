package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Contact;
import com.kienast.ecommercespringbootpaypalwebapp.entities.User;
import com.kienast.ecommercespringbootpaypalwebapp.services.AuthService;

@Controller
public class AuthController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private AuthService authService;
	
	@PostMapping(path="/register_user", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String register(@RequestParam MultiValueMap<String, String> userInfo, Model model) {
		
		Map<String, String> registeredUserDetails = userInfo.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
		
		Contact contact = new Contact();
		
		contact.setPhoneNumber(registeredUserDetails.get("phoneNumber"));
		contact.setEmail(registeredUserDetails.get("email"));
		contact.setLine1(registeredUserDetails.get("line1"));
		contact.setLine2(registeredUserDetails.get("line2"));
		contact.setCity(registeredUserDetails.get("city"));
		contact.setState(registeredUserDetails.get("state"));
		contact.setPostalCode(registeredUserDetails.get("postalCode"));
		contact.setCountry(registeredUserDetails.get("country"));
		
		LOGGER.info("Entered contact -> {}", contact);
		
		User user = new User();
		
		user.setUsername(registeredUserDetails.get("username"));
		user.setPassword(registeredUserDetails.get("password"));
		user.setFirstName(registeredUserDetails.get("firstName"));
		user.setLastName(registeredUserDetails.get("lastName"));
		user.setContact(contact);
		
		LOGGER.info("Entered user -> {}", user);

		User registeredUser = authService.register(user);
		
		if (null != registeredUser) {
			model.addAttribute("username", registeredUser.getUsername());
			return "shopHome";
		}
		
		model.addAttribute("message", "Login failed");
        return "loginError.html";
    }
	
	@PostMapping(path="/login", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String login(@RequestParam MultiValueMap<String, String> userCredentials) {
		/*
		if (userCredentials.get("username").get(0).equals("akdas") && userCredentials.get("password").get(0).equals("qwerty123")) {
			return true;
		}
		return false;
		*/
		return authService.login(null);
	}

}
