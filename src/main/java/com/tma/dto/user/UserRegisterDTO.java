package com.tma.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRegisterDTO {
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;
	
	@NotBlank
	private String password;
	
	public UserRegisterDTO() {}

	public UserRegisterDTO(String email, String firstname, String lastname, String password) {
		super();
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
}
