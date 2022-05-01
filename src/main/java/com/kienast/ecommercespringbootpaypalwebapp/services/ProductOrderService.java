package com.kienast.ecommercespringbootpaypalwebapp.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductOrder;
import com.kienast.ecommercespringbootpaypalwebapp.repositories.ProductOrderRepository;

@Component
public class ProductOrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductOrderService.class);

	@Autowired
	ProductOrderRepository productOrderRepository;
	
	public ProductOrder save(ProductOrder productOrder) {
		
		ProductOrder savedProductOrder = productOrderRepository.save(productOrder);
		LOGGER.info("Saved product order -> {}", savedProductOrder);

		return savedProductOrder;
	}
	
	public ProductOrder findById(Long productOrderId) {
		
		Optional<ProductOrder> optionalProductOrder = productOrderRepository.findById(productOrderId);
		LOGGER.info("Found product order -> {}", optionalProductOrder);

		if (optionalProductOrder.isPresent()) {
			return optionalProductOrder.get();
		}
		
		return null;
	}
	
	public List<ProductOrder> getAllProductOrders() {
		
		List<ProductOrder> allProductOrders = productOrderRepository.findAll();
		LOGGER.info("Found product orders -> {}", allProductOrders);
		
		return allProductOrders;
	}

}
