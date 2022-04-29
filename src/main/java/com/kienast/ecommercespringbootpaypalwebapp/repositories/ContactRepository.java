package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kienast.ecommercespringbootpaypalwebapp.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	

}
