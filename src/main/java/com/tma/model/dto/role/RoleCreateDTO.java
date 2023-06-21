package com.tma.model.dto.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleCreateDTO {

	@NotBlank(message = "name is required")
	private String name;
	

}
