package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kienast.ecommercespringbootpaypalwebapp.services.AuthService;

@Controller
public class StartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@GetMapping(path="/")
	public String showStartPage() {
		return "index.html";
	}
	
	@GetMapping(path="/register")
	public String showRegisterPage() {
		return "register.html";
	}
}
