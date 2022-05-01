package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kienast.ecommercespringbootpaypalwebapp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);	

}
