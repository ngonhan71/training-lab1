package com.tma.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tma.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
	Optional<Permission> findByPermissionCode(String permissionCode);
}
