package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kienast.ecommercespringbootpaypalwebapp.entities.OrderPayment;

public interface PaymentRepository extends JpaRepository<OrderPayment, Long> {
	
}
