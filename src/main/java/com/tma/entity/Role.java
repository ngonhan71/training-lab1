package com.tma.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "roles")
public class Role {
	
	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "role_id")
	private UUID roleId;
	
	@Column(name = "role_name", unique = true)
	private String roleName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "role_permission", 
			  joinColumns = @JoinColumn(name = "role_id"), 
			  inverseJoinColumns = @JoinColumn(name = "permission_id"))
	
	Set<Permission> permissions;
	
	public Role() {}
	
	public Role(UUID roleId, String roleName, Set<Permission> permissions) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.permissions = permissions;
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

}