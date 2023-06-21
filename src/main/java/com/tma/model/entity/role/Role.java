package com.tma.model.entity.role;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.tma.model.entity.BaseEntity;
import com.tma.model.entity.rolePermission.RolePermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Where(clause = "deleted=false")
public class Role extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "name", unique = true)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private List<RolePermission> permissions;
	
}