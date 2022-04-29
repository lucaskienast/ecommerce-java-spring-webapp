package com.kienast.ecommercespringbootpaypalwebapp.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Contact {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY, mappedBy="contact")
	private User user;
	
	private String phoneNumber;
	private String email;
	private String line1;
	private String line2;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	
	public Contact() {
		super();
	}

	public Contact(User user, String phoneNumber, String email, String line1, String line2, String city, String state,
			String postalCode, String country) {
		super();
		this.user = user;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", user=" + user + ", phoneNumber=" + phoneNumber + ", email=" + email + ", line1="
				+ line1 + ", line2=" + line2 + ", city=" + city + ", state=" + state + ", postalCode=" + postalCode
				+ ", country=" + country + "]";
	}

}
