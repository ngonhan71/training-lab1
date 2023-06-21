package com.tma.service.role;

import java.util.UUID;

import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import com.tma.model.dto.role.RoleCreateDTO;
import org.springframework.data.domain.Pageable;


public interface RoleService {

	ResponseModelDTO createNewRole(RoleCreateDTO roleCreateDTO);

	void softDeleteById(UUID id) throws NotFoundException;

	ResponseModelDTO findById(UUID id) throws NotFoundException;

	ResponsePageDTO findAll(Pageable pageable);

}
