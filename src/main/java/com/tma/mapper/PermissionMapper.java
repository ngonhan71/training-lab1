package com.tma.mapper;

import com.tma.model.dto.permission.PermissionCreateDTO;
import com.tma.model.dto.permission.PermissionDetailDTO;
import com.tma.model.entity.permission.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission fromCreateToEntity(PermissionCreateDTO permissionCreateDTO);

    PermissionDetailDTO fromEntityToDetail(Permission permission);

}
