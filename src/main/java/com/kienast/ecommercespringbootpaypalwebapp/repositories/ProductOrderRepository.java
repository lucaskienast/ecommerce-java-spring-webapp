package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
	
}
