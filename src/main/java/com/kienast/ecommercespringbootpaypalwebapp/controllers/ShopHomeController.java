package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kienast.ecommercespringbootpaypalwebapp.services.AuthService;

@Controller
public class ShopHomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@GetMapping(path="/shopHome")
	public String showShopHome() {
		return "shopHome";
	}
	
	@GetMapping(path="/mens_shoes")
	public String showMensShoes() {
		return "mens_shoes";
	}

}
