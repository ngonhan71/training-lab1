package com.tma.service.permission;

import java.util.UUID;

import com.tma.exception.NotFoundException;
import com.tma.model.dto.permission.PermissionCreateDTO;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import org.springframework.data.domain.Pageable;


public interface PermissionService {


	ResponseModelDTO createNewPermission(PermissionCreateDTO permissionCreateDTO);

	ResponseModelDTO updateById(UUID id, PermissionCreateDTO permissionCreateDTO) throws NotFoundException;

	ResponseModelDTO findById(UUID id) throws NotFoundException;

	ResponsePageDTO findAll(String keyword, Pageable pageable);

	void softDeleteById(UUID id) throws NotFoundException;

}
