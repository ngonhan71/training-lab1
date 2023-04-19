package com.tma.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tma.entity.Role;

public interface IRoleService {

	void deleteById(UUID id);

	Role findById(UUID id);

	Page<Role> findAll(Pageable pageable);

	List<Role> findAll();

	<S extends Role> S save(S entity);

	Role findByRoleName(String roleName);

	boolean grantPermission(UUID roleId, UUID permissionId);

	boolean revokePermission(UUID roleId, UUID permissionId);

	boolean updatePermission(UUID roleId, List<UUID> permissionIds);

}
