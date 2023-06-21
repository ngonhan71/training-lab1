package com.tma.model.dto.permission;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PermissionCreateDTO {
	
	@NotBlank(message = "code is required")
	private String code;
	
	@NotBlank(message = "name is required")
	private String name;
	

}
