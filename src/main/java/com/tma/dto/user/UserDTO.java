package com.tma.dto.user;

import java.util.UUID;

import com.tma.entity.Role;

public class UserDTO {
	private UUID userId;
	private String email;
	private String firstname;
	private String lastname;
	private Role role;
	private boolean isEnabled;
	
	public UserDTO() {}

	public UserDTO(UUID userId, String email, String firstname, String lastname, Role role, boolean isEnabled) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
		this.isEnabled = isEnabled;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
