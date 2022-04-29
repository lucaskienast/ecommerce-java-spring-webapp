package com.kienast.ecommercespringbootpaypalwebapp.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Contact;
import com.kienast.ecommercespringbootpaypalwebapp.entities.User;
import com.kienast.ecommercespringbootpaypalwebapp.repositories.ContactRepository;
import com.kienast.ecommercespringbootpaypalwebapp.repositories.UserRepository;

@Component
public class AuthService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	public String login(User user) {
		// get all users
		List<User> allUsers = userRepository.findAll();
		
		for (User u : allUsers) {
			LOGGER.info("{}", u);
		}
		
		// look for username in users
		
		// return null when no username found
		
		// check if found user password matches given
		
		// return null when incorrect
		
		// return user bean if correct
		
		return "login";
	}
	
	public User register(User user) {
		
		Contact contact = contactRepository.save(user.getContact());
		LOGGER.info("Saved contact -> {}", contact);

		User savedUser = userRepository.save(user);
		LOGGER.info("Saved user -> {}", savedUser);
		
		return savedUser;
	}

}
