package com.marketpay.services.mail.resource;

/**
 * Bean contenant les infos pour la signature du mail
 */
public class EmailSignature {
	
	private String name;
	
	private String address;
	
	private String postalCodeAndCity;
	
	private String country;
	
	private String email;
	
	private String phoneNumber;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCodeAndCity() {
		return postalCodeAndCity;
	}

	public void setPostalCodeAndCity(String postalCodeAndCity) {
		this.postalCodeAndCity = postalCodeAndCity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
