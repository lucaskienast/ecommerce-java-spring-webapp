package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

	@GetMapping(path="/")
	public String showStartPage() {
		return "index.html";
	}
	
	@GetMapping(path="/register")
	public String showRegisterPage() {
		return "register.html";
	}
}
