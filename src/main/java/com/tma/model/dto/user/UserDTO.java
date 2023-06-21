package com.tma.model.dto.user;

import java.util.UUID;

import com.tma.model.dto.role.RoleDetailDTO;
import lombok.Data;

@Data
public class UserDTO  {
	private UUID userId;
	private String email;
	private String firstname;
	private String lastname;
	private RoleDetailDTO role;
	private boolean isEnabled;

}
