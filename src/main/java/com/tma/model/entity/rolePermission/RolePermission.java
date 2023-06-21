package com.tma.model.entity.rolePermission;

import com.tma.model.entity.BaseEntity;
import com.tma.model.entity.permission.Permission;
import com.tma.model.entity.role.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_permission")
@Where(clause = "deleted=false")
public class RolePermission extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Permission permission;

}
