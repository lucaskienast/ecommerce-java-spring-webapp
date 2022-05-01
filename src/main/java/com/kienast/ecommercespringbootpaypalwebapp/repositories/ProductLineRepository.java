package com.kienast.ecommercespringbootpaypalwebapp.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductCategory;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductLine;

public interface ProductLineRepository extends JpaRepository<ProductLine, Long> {
	
	ProductLine findById(String productLineId);	

	List<ProductLine> findByCategory(ProductCategory category);	

}
