package com.tma.service.role;

import java.util.*;

import com.tma.exception.NotFoundException;
import com.tma.mapper.PermissionMapper;
import com.tma.mapper.RoleMapper;
import com.tma.model.dto.permission.PermissionDetailDTO;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import com.tma.model.dto.role.RoleBasicDTO;
import com.tma.model.dto.role.RoleCreateDTO;
import com.tma.model.dto.role.RoleDetailDTO;
import com.tma.exception.BadRequestException;
import com.tma.model.entity.permission.Permission;
import com.tma.model.entity.rolePermission.RolePermission;
import com.tma.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tma.model.entity.role.Role;
import com.tma.repository.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public ResponsePageDTO findAll(Pageable pageable) {
		Page<Role> rolePage = roleRepository.findAll(pageable);

		List<RoleBasicDTO> roleBasicDTOS = new ArrayList<>();

		for(Role role : rolePage.getContent()) {
			RoleBasicDTO roleBasicDTO = roleMapper.fromEntityToBasic(role);
			roleBasicDTOS.add(roleBasicDTO);
		}

		return ResponsePageDTO.builder()
				.data(roleBasicDTOS)
				.limit(rolePage.getSize())
				.currentPage(rolePage.getNumber())
				.totalItems(rolePage.getTotalElements())
				.totalPages(rolePage.getTotalPages())
				.build();
	}

	@Override
	public ResponseModelDTO findById(UUID id) throws NotFoundException {
		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Role not found: " + id));

		RoleDetailDTO roleDetailDTO = roleMapper.fromEntityToDetail(role);

		List<PermissionDetailDTO> permissionDetailDTOList = new ArrayList<>();

		for(RolePermission rolePermission : role.getPermissions()) {
			PermissionDetailDTO permissionDetailDTO = permissionMapper.fromEntityToDetail(rolePermission.getPermission());
			permissionDetailDTOList.add(permissionDetailDTO);
		}
		roleDetailDTO.setPermissions(permissionDetailDTOList);

		return ResponseModelDTO.builder()
				.data(roleDetailDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO createNewRole(RoleCreateDTO roleCreateDTO) {
		if (roleRepository.existsByName(roleCreateDTO.getName())) {
			throw new BadRequestException("Role with this name already exists");
		}

		Role role = roleMapper.fromCreateToEntity(roleCreateDTO);
		RoleBasicDTO roleBasicDTO = roleMapper.fromEntityToBasic(roleRepository.save(role));
		return ResponseModelDTO.builder()
				.data(roleBasicDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	@Transactional
	public void softDeleteById(UUID id) throws NotFoundException {
		if (permissionRepository.existsById(id))
			permissionRepository.softDeleteById(id);
		else throw new NotFoundException("Not found role with id: " + id);
	}
	
}
