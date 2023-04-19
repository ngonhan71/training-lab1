package com.tma.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permissions")
public class Permission {
	
	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "permission_id")
	private UUID permissionId;
	
	@Column(name = "permission_code", unique = true)
	private String permissionCode;
	
	@Column(name = "permission_name")
	private String permissionName;
	
	@ManyToMany(mappedBy = "permissions")
	@JsonIgnore
	Set<Role> roles;
	
	public Permission() {}

	public Permission(UUID permissionId, String permissionCode, String permissionName, Set<Role> roles) {
		super();
		this.permissionId = permissionId;
		this.permissionCode = permissionCode;
		this.permissionName = permissionName;
		this.roles = roles;
	}


	public UUID getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(UUID permissionId) {
		this.permissionId = permissionId;
	}
	

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}

