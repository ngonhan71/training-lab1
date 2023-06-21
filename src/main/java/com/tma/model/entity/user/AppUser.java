package com.tma.model.entity.user;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import com.tma.model.entity.BaseEntity;
import com.tma.model.entity.role.Role;
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
@Table(name = "users")
@Where(clause = "deleted=false")
public class AppUser extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id")
	private UUID id;
	
	private String firstname;
	
	private String lastname;
	
	@Column(name = "email", unique = true, length = 255)
	private String email;
	
	private String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@Column(name = "verification_code", length = 64)
    private String verificationCode;

	@Column(name = "reset_password_code", length = 64)
	private String  resetPasswordCode;
	

}
