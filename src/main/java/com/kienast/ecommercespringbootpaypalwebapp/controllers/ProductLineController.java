package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Product;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductLine;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductLineService;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductService;

@Controller
public class ProductLineController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLineController.class);

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductLineService productLineService;
	
	@GetMapping(path="/products")
	public String showProducts(@RequestParam Long productLineId, Model model) {
		
		LOGGER.info("Given product line ID -> {}", productLineId);

		ProductLine productLine = productLineService.getProductLineById(productLineId);
		LOGGER.info("Found product line -> {}", productLine);
		
		if (null == productLine) {
			model.addAttribute("message", "Loading product failed");
	        return "shopError";
		}
		
		List<Product> shoes = productService.getAllProductsFromProductLine(productLineId);
		LOGGER.info("Found shoes of {} -> {}", productLineId, shoes);
		
		if (null != shoes) {
			model.addAttribute("productLine", productLine);
			model.addAttribute("products", shoes);
			return "product";
		}

		model.addAttribute("message", "Loading product failed");
        return "shopError";
	}
}
