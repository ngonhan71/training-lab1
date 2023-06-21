package com.tma.controller;

import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tma.model.dto.role.RoleCreateDTO;
import com.tma.service.role.RoleService;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	@Operation(summary = "Get all roles")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> getAll(
			@RequestParam(name = "page", defaultValue = "0") int page, //page number
			@RequestParam(name = "limit", defaultValue = "20") int limit, //page size
			@RequestParam(name = "orderBy", defaultValue = "name") String orderBy, //database field
			@RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
	) {
		Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);

		Pageable pageable = PageRequest.of(page, limit, sort);
		return ResponseEntity.ok().body(roleService.findAll(pageable));

	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get a role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Find a role successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "404", description = "Not Found")

	})
	public ResponseEntity<ResponseModelDTO> findById(@PathVariable UUID id) throws NotFoundException {
		return ResponseEntity.ok().body(roleService.findById(id));
	}
	
	@PostMapping
	@Operation(summary = "Create a new role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Create a new role successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad request")

	})
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<ResponseModelDTO> create(@Valid @RequestBody RoleCreateDTO roleCreateDTO) {

		return ResponseEntity.ok().body(roleService.createNewRole(roleCreateDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete a role successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "404", description = "Not Found")

	})
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable UUID id) throws JsonProcessingException, NotFoundException {
		roleService.softDeleteById(id);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree("{\"message\":\"Delete role successfully\"}");

		return ResponseEntity.ok().body(json);
	}
	
}
