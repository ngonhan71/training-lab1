package com.tma.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tma.dto.role.RoleCreateDTO;
import com.tma.entity.Role;
import com.tma.response.ResponseModel;
import com.tma.service.IRoleService;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	IRoleService roleService;
	
	@GetMapping("")
	@Operation(summary = "Get all roles")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<List<Role>>(true, "Successfully", roleService.findAll()));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get a role")
	public ResponseEntity<?> getById(@PathVariable UUID id) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<Role>(true, "Successfully", roleService.findById(id)));
	}
	
	@PostMapping("")
	@Operation(summary = "Create a new role")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> create(@Valid @RequestBody RoleCreateDTO roleCreateDTO) {
		
		Role entity = modelMapper.map(roleCreateDTO, Role.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<Role>(true, "Successfully", roleService.save(entity)));
	}

	
	@PostMapping("/{roleId}/grant-permission/{permissionId}")
	@Operation(summary = "Grant permission to role")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> grantPermission(@Valid @PathVariable UUID roleId, @PathVariable UUID permissionId) {

		if (roleService.grantPermission(roleId, permissionId)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Successfully", null));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseModel<>(false, "Failed", null));
	}

	@PutMapping("/{roleId}/update-permission")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> updatePermission(@Valid @PathVariable UUID roleId, @RequestBody List<UUID> permissions) {

		if (roleService.updatePermission(roleId, permissions)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Update permission Successfully", null));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseModel<>(false, "Failed", null));
	}
	@DeleteMapping("/{roleId}/revoke-permission/{permissionId}")
	@Operation(summary = "Revoke permission from role")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> revokePermission(@PathVariable UUID roleId, @PathVariable UUID permissionId) {

		if (roleService.revokePermission(roleId, permissionId)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Successfully", null));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseModel<>(false, "Failed", null));

	}
	
}
