package com.tma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.exception.NotFoundException;
import com.tma.model.dto.permission.PermissionIdsDTO;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.rolePermission.RolePermissionCreateDTO;
import com.tma.service.rolePermission.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    @Operation(summary = "Create role permission", description = "Create role permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public ResponseEntity<ResponseModelDTO> createRole(
            @Valid @RequestBody RolePermissionCreateDTO rolePermissionCreateDTO) throws NotFoundException {
        return ResponseEntity.ok().body(rolePermissionService.createRolePermission(rolePermissionCreateDTO));
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/{roleId}")
    @Operation(summary = "Update permissions by role id", description = "Update permissions by role id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    public ResponseEntity<ResponseModelDTO> updatePermissionByRoleId(
            @PathVariable UUID roleId,
            @Valid @RequestBody PermissionIdsDTO permissionIdsDTO) throws NotFoundException {
        return ResponseEntity.ok().body(rolePermissionService.updatePermissionByRoleId(roleId, permissionIdsDTO.getPermissionIds()));
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role and permission by id", description = "Delete role and permission by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JsonNode.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NotFound")
    })
    public ResponseEntity<?> deleteRoleAndPermissionById(
            @PathVariable UUID id) throws NotFoundException, JsonProcessingException {
        rolePermissionService.softDeleteById(id);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"Message\":\"Delete successfully\"}");
        return ResponseEntity.ok().body(json);
    }


}
