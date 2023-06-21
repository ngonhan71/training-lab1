package com.tma.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tma.model.entity.role.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
	boolean existsByName(@Param("name") String name);

	Optional<Role> findByName(String name);

	@Modifying
	@Query("UPDATE Role r SET r.deleted = true where r.id = ?1")
	void softDeleteById(UUID id);
}
