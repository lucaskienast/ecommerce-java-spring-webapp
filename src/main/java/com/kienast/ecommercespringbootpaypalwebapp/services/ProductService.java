package com.kienast.ecommercespringbootpaypalwebapp.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Product;
import com.kienast.ecommercespringbootpaypalwebapp.repositories.ProductRepository;

@Component
public class ProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProducts() {
		
		List<Product> allProducts = productRepository.findAll();
		LOGGER.info("Found products -> {}", allProducts);
		
		return allProducts;
	}
	
	public List<Product> getAllProductsFromProductLine(Long productLineId) {
		
		List<Product> products = productRepository.findByProductLineId(productLineId);
		LOGGER.info("Number of sizes of product {} -> {}", productLineId, products.size());

		return products;
	}
	
	public Product getShoeById(Long productId) {
		
		Optional<Product> optionalShoe = productRepository.findById(productId);
		LOGGER.info("Found product -> {}", optionalShoe);
		
		if (optionalShoe.isPresent()) {
			return optionalShoe.get();
		}
		
		return null;
	}

}
