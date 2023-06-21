package com.tma.service.rolePermission;

import com.tma.exception.BadRequestException;
import com.tma.exception.NotFoundException;
import com.tma.mapper.PermissionMapper;
import com.tma.mapper.RoleMapper;
import com.tma.mapper.RolePermissionMapper;
import com.tma.model.dto.permission.PermissionDetailDTO;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.role.RoleDetailDTO;
import com.tma.model.dto.rolePermission.RolePermissionCreateDTO;
import com.tma.model.dto.rolePermission.RolePermissionDetailDTO;
import com.tma.model.entity.permission.Permission;
import com.tma.model.entity.role.Role;
import com.tma.model.entity.rolePermission.RolePermission;
import com.tma.repository.PermissionRepository;
import com.tma.repository.RolePermissionRepository;
import com.tma.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public ResponseModelDTO createRolePermission(RolePermissionCreateDTO rolePermissionCreateDTO) throws NotFoundException {

        UUID roleId = rolePermissionCreateDTO.getRoleId();
        UUID permissionId = rolePermissionCreateDTO.getPermissionId();

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new NotFoundException("Permission not found: " + permissionId));

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw new BadRequestException("The permission already exists in this role");
        }

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        return ResponseModelDTO.builder()
                .data(rolePermissionMapper.fromEntityToDetail(rolePermissionRepository.save(rolePermission)))
                .isSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public ResponseModelDTO updatePermissionByRoleId(UUID roleId, List<UUID> permissionList) throws NotFoundException {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleId));


        List<RolePermission> newRolePermissions = new ArrayList<>();

        for(UUID uuid : permissionList) {
			Permission permission = permissionRepository.findById(uuid)
					.orElseThrow(() -> new NotFoundException("Permission not found: " + uuid));

            RolePermission rolePermission = RolePermission.builder()
                    .role(role)
                    .permission(permission)
                    .build();

            newRolePermissions.add(rolePermission);
		}

        role.getPermissions().clear();
        rolePermissionRepository.deleteRolePermissionByRoleId(roleId);

        List<RolePermission> rolePermissions = rolePermissionRepository.saveAll(newRolePermissions);

        List<PermissionDetailDTO> permissionDetailDTOS  = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissions) {
            permissionDetailDTOS.add(permissionMapper.fromEntityToDetail(rolePermission.getPermission()));
        }

        return ResponseModelDTO.builder()
                .data(permissionDetailDTOS)
                .isSuccess(true)
                .build();

    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) throws NotFoundException {
        if (rolePermissionRepository.existsById(id))
            rolePermissionRepository.softDeleteById(id);
        else throw new NotFoundException("Not found role with id: " + id);
    }
}
