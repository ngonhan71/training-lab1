package com.tma.mapper;

import com.tma.model.dto.rolePermission.RolePermissionDetailDTO;
import com.tma.model.entity.rolePermission.RolePermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    RolePermissionDetailDTO fromEntityToDetail(RolePermission permission);

}
