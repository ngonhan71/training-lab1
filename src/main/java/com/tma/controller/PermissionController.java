package com.tma.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tma.dto.permission.PermissionCreateDTO;
import com.tma.entity.Permission;
import com.tma.response.ResponseModel;
import com.tma.service.IPermissionService;


@RestController
@RequestMapping("api/v1/permissions")
public class PermissionController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	IPermissionService permissionService;
	
	@GetMapping("")
	@Operation(summary = "Get all permissions")
	public ResponseEntity<?> getAll(
			@RequestParam(defaultValue = "1") @Positive int page, 
			@RequestParam(defaultValue = "5") @Positive int limit) {
		Pageable paging = PageRequest.of(page - 1, limit, Sort.by("permissionCode").ascending());
		
		List<Permission> permissions = new ArrayList<Permission>();
		Page<Permission> pagePermissions = permissionService.findAll(paging);

		permissions = pagePermissions.getContent();
		Map<String, Object> response = new HashMap<>();
		response.put("permissions", permissions);
		response.put("currentPage", pagePermissions.getNumber() + 1);
		response.put("totalItems", pagePermissions.getTotalElements());
		response.put("totalPages", pagePermissions.getTotalPages());
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", response));
	}
	
	@PostMapping("")
	@Operation(summary = "Create a new permission")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> create(@Valid @RequestBody PermissionCreateDTO permissionCreateDTO) {
		
		Permission permission = modelMapper.map(permissionCreateDTO, Permission.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<Permission>(true, "Successfully", permissionService.save(permission)));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a permission")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody PermissionCreateDTO permissionCreateDTO) {

		Permission permission = modelMapper.map(permissionCreateDTO, Permission.class);
		permission.setPermissionId(id);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<Permission>(true, "Successfully", permissionService.save(permission)));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a permission")
	@PreAuthorize("hasAuthority('PERMISSION_MANAGEMENT')")
	public ResponseEntity<?> delete(@PathVariable UUID id) {
		try {
			permissionService.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Successfully"));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseModel<>(false, ex.getMessage()));
		}
	}
}
