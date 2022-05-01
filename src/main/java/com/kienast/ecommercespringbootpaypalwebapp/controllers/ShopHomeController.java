package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductLine;
import com.kienast.ecommercespringbootpaypalwebapp.services.ShopHomeService;

@Controller
public class ShopHomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopHomeController.class);

	@Autowired
	private ShopHomeService shopHomeService;
	
	@GetMapping(path="/shopHome")
	public String showShopHome() {
		return "shopHome";
	}
	
	@GetMapping(path="/mens_shoes")
	public String showMensShoes(Model model) {
		
		List<ProductLine> mensShoes = shopHomeService.getAllMensShoes();
		LOGGER.info("Found men's shoes -> {}", mensShoes);

		if (null != mensShoes) {
			model.addAttribute("title", "Men's Shoes");
			model.addAttribute("productLines", mensShoes);
			return "productLines";
		}

		model.addAttribute("message", "Loading product lines failed");
        return "shopError";	
	}
	
	@GetMapping(path="/womens_shoes")
	public String showWomensShoes(Model model) {
		
		List<ProductLine> womensShoes = shopHomeService.getAllWomensShoes();
		LOGGER.info("Found women's shoes -> {}", womensShoes);

		if (null != womensShoes) {
			model.addAttribute("title", "Women's Shoes");
			model.addAttribute("productLines", womensShoes);
			return "productLines";
		}

		model.addAttribute("message", "Loading product lines failed");
        return "shopError";	
	}

}
