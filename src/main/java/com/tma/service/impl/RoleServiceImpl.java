package com.tma.service.impl;

import java.util.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.tma.entity.Permission;
import com.tma.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tma.entity.Role;
import com.tma.repository.RoleRepository;
import com.tma.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public <S extends Role> S save(S entity) {
		Optional<Role> roleOptional = roleRepository.findByRoleName(entity.getRoleName());
		
		if (roleOptional.isPresent()) {
			throw new EntityExistsException("Role đã tồn tại!");
		}
		return roleRepository.save(entity);
	}

	@Override
	public Role findByRoleName(String roleName) {
		Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
		if (roleOptional.isPresent()) return roleOptional.get();
		throw new EntityNotFoundException("Role không tồn tại!");
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	@Override
	public Role findById(UUID id) {
		Optional<Role> roleOptional = roleRepository.findById(id);
		
		if (roleOptional.isPresent()) return roleOptional.get();
		
		throw new EntityNotFoundException("Role không tồn tại!");
	}

	@Override
	public boolean grantPermission(UUID roleId, UUID permissionId) {
		Optional<Role> roleOptional = roleRepository.findById(roleId);
		Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);

		if (roleOptional.isPresent() && permissionOptional.isPresent()) {
			Role role = roleOptional.get();

			Set<Permission> permissions = role.getPermissions();

			permissions.add(permissionOptional.get());

			role.setPermissions(permissions);

			roleRepository.save(role);

			return true;
		}

		return false;
	}

	@Override
	public boolean revokePermission(UUID roleId, UUID permissionId) {
		Optional<Role> roleOptional = roleRepository.findById(roleId);
		Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);

		if (roleOptional.isPresent() && permissionOptional.isPresent()) {
			Role role = roleOptional.get();

			Set<Permission> permissions = role.getPermissions();

			permissions.remove(permissionOptional.get());

			role.setPermissions(permissions);

			roleRepository.save(role);

			return true;
		}

		return false;
	}

	@Override
	public boolean updatePermission(UUID roleId, List<UUID> permissionIds) {

		Optional<Role> roleOptional = roleRepository.findById(roleId);

		Set<Permission> permissions = new HashSet<>();

		if (roleOptional.isPresent()) {
			Role role = roleOptional.get();

			for(UUID uuid : permissionIds) {
				Optional<Permission> permissionOptional = permissionRepository.findById(uuid);
				if (permissionOptional.isPresent()) {
					permissions.add(permissionOptional.get());
				}
			}
			role.setPermissions(permissions);

			roleRepository.save(role);
			return true;

		}

		return false;

	}

	@Override
	public void deleteById(UUID id) {
		roleRepository.deleteById(id);
	}
	
}
