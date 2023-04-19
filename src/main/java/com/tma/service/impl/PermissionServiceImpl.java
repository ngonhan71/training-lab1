package com.tma.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tma.entity.Permission;
import com.tma.repository.PermissionRepository;
import com.tma.service.IPermissionService;

@Service
public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public <S extends Permission> S save(S entity) {
		Optional<Permission> permissionOptional = permissionRepository.findByPermissionCode(entity.getPermissionCode());
		
		if (permissionOptional.isPresent()) {
			if (entity.getPermissionId().compareTo(permissionOptional.get().getPermissionId()) != 0)
				throw new EntityExistsException("Permission đã tồn tại!");
		}
		
		return permissionRepository.save(entity);
	}

	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	public Page<Permission> findAll(Pageable pageable) {
		return permissionRepository.findAll(pageable);
	}

	@Override
	public Permission findById(UUID id) {
		Optional<Permission> permissionOptional = permissionRepository.findById(id);
		
		if (permissionOptional.isPresent()) return permissionOptional.get();
		
		throw new EntityNotFoundException("Permission không tồn tại!");
		
	}

	@Override
	public void deleteById(UUID id) {
		permissionRepository.deleteById(id);
	}
	
	
	
}
