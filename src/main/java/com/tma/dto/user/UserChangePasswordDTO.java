package com.tma.dto.user;

import javax.validation.constraints.NotBlank;

public class UserChangePasswordDTO {

	@NotBlank
	private String currentPassword;
	
	@NotBlank
	private String newPassword;
	
	public UserChangePasswordDTO() {}

	public UserChangePasswordDTO(String currentPassword, String newPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
