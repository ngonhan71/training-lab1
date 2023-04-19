package com.tma.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tma.entity.Permission;

public interface IPermissionService {

	void deleteById(UUID id);

	Permission findById(UUID id);

	Page<Permission> findAll(Pageable pageable);

	List<Permission> findAll();

	<S extends Permission> S save(S entity);

}
