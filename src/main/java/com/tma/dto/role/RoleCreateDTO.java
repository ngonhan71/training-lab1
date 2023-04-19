package com.tma.dto.role;

import javax.validation.constraints.NotBlank;

public class RoleCreateDTO {
	
	@NotBlank
	private String roleName;
	
	public RoleCreateDTO() {}


	public RoleCreateDTO( String roleName) {
		super();
		this.roleName = roleName;
	}


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
