package com.kienast.ecommercespringbootpaypalwebapp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ProductOrder {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private int amount;
	
	@Column(nullable=false)
	private float price;
	
	@Column(nullable=false)
	private float subtotal;
	
	@Column(nullable=false)
	private float shipping;
	
	@Column(nullable=false)
	private float tax;
	
	@Column(nullable=false)
	private float total;
	
	@ManyToOne
	private Product product;
	
	@OneToOne(fetch=FetchType.EAGER, mappedBy="productOrder")
	private OrderPayment payment;

	public ProductOrder() {
		super();
	}

	public ProductOrder(int amount, float price, float subtotal, float shipping, float tax, float total, Product product) {
		super();
		this.amount = amount;
		this.price = price;
		this.subtotal = subtotal;
		this.shipping = shipping;
		this.tax = tax;
		this.total = total;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getShipping() {
		return shipping;
	}

	public void setShipping(float shipping) {
		this.shipping = shipping;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", amount=" + amount + ", subtotal=" + subtotal + ", shipping=" + shipping + ", tax="
				+ tax + ", total=" + total + ", product=" + product + "]";
	}
	
}
