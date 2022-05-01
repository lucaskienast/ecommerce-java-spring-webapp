package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.kienast.ecommercespringbootpaypalwebapp.entities.Product;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductCategory;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductService;

@Controller
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	
	@PostMapping(path="/buy_product", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String addProductToCart(@RequestParam MultiValueMap<String, String> userInfo, Model model) {
		
		Map<String, String> cartProductDetails = userInfo.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		LOGGER.info("Cart product -> {}", cartProductDetails);
		LOGGER.info("Cart product id -> {}", cartProductDetails.get("productId"));
		LOGGER.info("Cart product amount -> {}", cartProductDetails.get("amount"));
		LOGGER.info("Cart product price -> {}", cartProductDetails.get("price"));
		
		int amount = Integer.parseInt(cartProductDetails.get("amount"));
		float price = Float.parseFloat(cartProductDetails.get("price"));
		float shipping = 5.00f;
		float tax = 1.00f;
		float total = amount * price + shipping + tax;
		
		model.addAttribute("productId", cartProductDetails.get("productId"));
		model.addAttribute("amount", cartProductDetails.get("amount"));
		model.addAttribute("price", price);
		model.addAttribute("subtotal", amount * price);
		model.addAttribute("shipping", shipping);
		model.addAttribute("tax", tax);
		model.addAttribute("total", total);
		
		return "checkout";
	}
	
	@PostMapping(path="/filter_products_category", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String filterProductsByCategory(@RequestParam MultiValueMap<String, String> filterCriteria, Model model) {
		
		if (null == filterCriteria.get("category").get(0)) {
			model.addAttribute("message", "Product filter failed");
	        return "adminError";
		}
		
		LOGGER.info("Given category -> {}", filterCriteria.get("category").get(0));
		
		List<Product> products = productService.getAllProducts();
		LOGGER.info("Found products -> {}", products);

		List<Product> filteredProducts = new ArrayList<>(); 
		
		for (Product product : products) {
			LOGGER.info("Product category -> {}", product.getProductLine().getCategory());
			if (filterCriteria.get("category").get(0).equals("MENS_SHOES") 
					&& product.getProductLine().getCategory().equals(ProductCategory.MENS_SHOES)) {
				filteredProducts.add(product);
			}
			else if (filterCriteria.get("category").get(0).equals("WOMENS_SHOES")
					&& product.getProductLine().getCategory().equals(ProductCategory.WOMENS_SHOES)) {
				filteredProducts.add(product);
			}
		}
		
		LOGGER.info("Filtered products -> {}", filteredProducts);
		
		if (filteredProducts.size() >= 1) {
			model.addAttribute("products", filteredProducts);
			return "productList";
		}

		model.addAttribute("message", "Product filter failed");
        return "adminError";
	}
	
	@GetMapping(path="/all_products")
	public String showAllProducts(Model model) {
				
		List<Product> allProducts = productService.getAllProducts();
		LOGGER.info("Found products -> {}", allProducts);
		
		if (null != allProducts) {
			model.addAttribute("products", allProducts);
			return "productList";
		}

		model.addAttribute("message", "Loading products failed");
        return "adminError";
	}
}
