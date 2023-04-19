package com.tma.dto.user;

import javax.validation.constraints.NotBlank;

public class UserUpdateDTO {

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;
	
	public UserUpdateDTO() {}

	public UserUpdateDTO(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
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
}
