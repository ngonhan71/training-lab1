package com.tma.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tma.model.entity.permission.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

	@Query(value = "SELECT p FROM Permission p WHERE unaccent(lower(p.name)) LIKE unaccent(lower(:keyword))")
	Page<Permission> findAll(String keyword, Pageable pageable);

	Optional<Permission> findByCode(String code);

	boolean existsByCode(@Param("code") String code);

	@Modifying
	@Query("UPDATE Permission p SET p.deleted = true where p.id = ?1")
	void softDeleteById(UUID id);
}
