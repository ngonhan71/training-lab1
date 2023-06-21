package com.tma.service.rolePermission;

import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.rolePermission.RolePermissionCreateDTO;

import java.util.List;
import java.util.UUID;

public interface RolePermissionService {
    ResponseModelDTO createRolePermission(RolePermissionCreateDTO rolePermissionCreateDTO) throws NotFoundException;

    ResponseModelDTO updatePermissionByRoleId(UUID roleId, List<UUID> permissionList) throws NotFoundException;

    void softDeleteById(UUID id) throws NotFoundException;
}
