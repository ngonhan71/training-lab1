package com.tma.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tma.entity.AppUser;

import javax.mail.MessagingException;

public interface IAppUserService {

	AppUser findById(UUID id);

	Page<AppUser> findAll(Pageable pageable);

	List<AppUser> findAll();

	AppUser create(AppUser entity);

	AppUser findByEmail(String email);

	AppUser findByResetPasswordCode(String code);

	boolean verify(String verificationCode);

	boolean changePassword(String email, String password);

	AppUser updateProfile(AppUser data);

	boolean updateResetPasswordCode(String email, String code);

}
