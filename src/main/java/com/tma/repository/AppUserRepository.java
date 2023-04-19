package com.tma.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tma.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
	Optional<AppUser> findByEmail(String email);
	Optional<AppUser> findByVerificationCode(String code);

	Optional<AppUser> findByResetPasswordCode(String code);

	@Transactional
	@Modifying
	@Query("update AppUser u set u.resetPasswordCode = :code WHERE u.email = :email")
	int updateResetPasswordCode(@Param("email") String email,@Param("code") String code);
	
	@Transactional
	@Modifying
	@Query("update AppUser u set u.password = :password WHERE u.email = :email")
	int changePassword(@Param("email") String email,@Param("password") String password);
	
}
