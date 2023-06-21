package com.tma.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterDTO {

	@NotBlank(message = "email is required")
	@Email
	private String email;

	@NotBlank(message = "firstname is required")
	private String firstname;

	@NotBlank(message = "lastname is required")
	private String lastname;

	@NotBlank(message = "password is required")
	private String password;
	

}
