package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByProductLineId(Long productLineId);	
	
}
