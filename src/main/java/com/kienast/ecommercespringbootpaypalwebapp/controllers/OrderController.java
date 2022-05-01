package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductCategory;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductOrder;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductOrderService;

@Controller
public class OrderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@GetMapping(path="/all_orders")
	public String showAllOrders(Model model) {
				
		List<ProductOrder> allProductOrders = productOrderService.getAllProductOrders();
		LOGGER.info("Found product orders -> {}", allProductOrders);
		
		if (null != allProductOrders) {
			model.addAttribute("orders", allProductOrders);
			return "orderList";
		}

		model.addAttribute("message", "Loading orders failed");
        return "adminError";
	}
	
	@PostMapping(path="/filter_orders_category", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String filterOrdersByCategory(@RequestParam MultiValueMap<String, String> filterCriteria, Model model) {
		
		if (null == filterCriteria.get("category").get(0)) {
			model.addAttribute("message", "Order filter failed");
	        return "adminError";
		}
		
		LOGGER.info("Given category -> {}", filterCriteria.get("category").get(0));
		
		List<ProductOrder> orders = productOrderService.getAllProductOrders();
		LOGGER.info("Found orders -> {}", orders);

		List<ProductOrder> filteredOrders = new ArrayList<>(); 
		
		for (ProductOrder order : orders) {
			LOGGER.info("Order -> {}", order.getProduct().getProductLine().getCategory());
			if (filterCriteria.get("category").get(0).equals("MENS_SHOES") 
					&& order.getProduct().getProductLine().getCategory().equals(ProductCategory.MENS_SHOES)) {
				filteredOrders.add(order);
			}
			else if (filterCriteria.get("category").get(0).equals("WOMENS_SHOES")
					&& order.getProduct().getProductLine().getCategory().equals(ProductCategory.WOMENS_SHOES)) {
				filteredOrders.add(order);
			}
		}
		
		LOGGER.info("Filtered orders -> {}", filteredOrders);
		
		if (filteredOrders.size() >= 1) {
			model.addAttribute("orders", filteredOrders);
			return "orderList";
		}

		model.addAttribute("message", "Order filter failed");
        return "adminError";
	}

}
