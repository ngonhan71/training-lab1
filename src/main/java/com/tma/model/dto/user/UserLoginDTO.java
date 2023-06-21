package com.tma.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {

	@NotBlank(message = "email is required")
	private String email;

	@NotBlank(message = "password is required")
	private String password;

}
