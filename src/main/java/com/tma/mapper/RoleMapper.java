package com.tma.mapper;

import com.tma.model.dto.role.RoleBasicDTO;
import com.tma.model.dto.role.RoleCreateDTO;
import com.tma.model.dto.role.RoleDetailDTO;
import com.tma.model.entity.role.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role fromCreateToEntity(RoleCreateDTO roleCreateDTO);

    RoleBasicDTO fromEntityToBasic(Role role);

    RoleDetailDTO fromEntityToDetail(Role role);

}
