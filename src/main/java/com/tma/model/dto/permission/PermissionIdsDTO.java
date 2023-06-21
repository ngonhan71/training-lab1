package com.tma.model.dto.permission;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PermissionIdsDTO {
    List<UUID> permissionIds;
}
