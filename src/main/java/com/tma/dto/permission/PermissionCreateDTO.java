package com.tma.dto.permission;

import javax.validation.constraints.NotBlank;

public class PermissionCreateDTO {
	
	@NotBlank
	private String permissionCode;
	
	@NotBlank
	private String permissionName;
	
	public PermissionCreateDTO() {}

	public PermissionCreateDTO(String permissionCode, String permissionName) {
		super();
		this.permissionCode = permissionCode;
		this.permissionName = permissionName;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
}
