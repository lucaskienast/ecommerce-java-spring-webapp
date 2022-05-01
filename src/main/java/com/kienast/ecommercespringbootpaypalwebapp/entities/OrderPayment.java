package com.kienast.ecommercespringbootpaypalwebapp.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class OrderPayment {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	private ProductOrder productOrder;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(nullable=false)
	private float total;

	public OrderPayment() {
		super();
	}

	public OrderPayment(ProductOrder productOrder, float total) {
		super();
		this.productOrder = productOrder;
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public ProductOrder getProductOrder() {
		return productOrder;
	}

	public void setProductOrder(ProductOrder productOrder) {
		this.productOrder = productOrder;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "OrderPayment [id=" + id + ", productOrder=" + productOrder + ", createdAt=" + createdAt + ", total="
				+ total + "]";
	}

}
