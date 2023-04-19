package com.tma.dto.user;

import javax.validation.constraints.NotBlank;

public class UserLoginDTO {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	public UserLoginDTO() {}

	public UserLoginDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
