package com.tma.model.dto.rolePermission;

import com.tma.model.dto.permission.PermissionDetailDTO;
import com.tma.model.dto.role.RoleBasicDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionDetailDTO {

    private UUID id;

    private RoleBasicDTO role;

    private PermissionDetailDTO permission;

}
