package com.tma.controller;

import java.util.UUID;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
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

import com.tma.model.dto.permission.PermissionCreateDTO;
import com.tma.service.permission.PermissionService;


@RestController
@RequestMapping("api/v1/permissions")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping
	@Operation(summary = "Get all permissions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Find all equipments successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),

	})
	public ResponseEntity<ResponsePageDTO> getAll(
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, //page number
			@RequestParam(name = "limit", defaultValue = "20") int limit, //page size
			@RequestParam(name = "orderBy", defaultValue = "name") String orderBy, //database field
			@RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
	) {

		Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);

		Pageable pageable = PageRequest.of(page, limit, sort);
		return ResponseEntity.ok().body(permissionService.findAll(keyword, pageable));

	}
	
	@PostMapping
	@Operation(summary = "Create a new permission")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Create a new permission successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad request")

	})
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<ResponseModelDTO> create(@Valid @RequestBody PermissionCreateDTO permissionCreateDTO) {

		return ResponseEntity.ok().body(permissionService.createNewPermission(permissionCreateDTO));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a permission")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update a permission successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "404", description = "Not Found")

	})
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<ResponseModelDTO> update(@PathVariable UUID id, @Valid @RequestBody PermissionCreateDTO permissionCreateDTO) throws NotFoundException {

		return ResponseEntity.ok().body(permissionService.updateById(id, permissionCreateDTO));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a permission")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Delete a permission successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "404", description = "Not Found")

	})
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> delete(@PathVariable UUID id) throws JsonProcessingException, NotFoundException {
		permissionService.softDeleteById(id);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree("{\"message\":\"Delete permission successfully\"}");

		return ResponseEntity.ok().body(json);
	}
}
