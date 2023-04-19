package com.tma.service.impl;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.tma.factory.email.SendEmailService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tma.entity.AppUser;
import com.tma.repository.AppUserRepository;
import com.tma.service.IAppUserService;

@Service
public class AppUserServiceImpl implements IAppUserService {

	@Autowired
	AppUserRepository appUserRepository;

	@Override
	public AppUser findByEmail(String email) {
		Optional<AppUser> userOptional = appUserRepository.findByEmail(email);
		
		if (userOptional.isPresent()) return userOptional.get();
		
		throw new EntityNotFoundException("User không tồn tại!");
	}

	@Override
	public AppUser findByResetPasswordCode(String code) {
		Optional<AppUser> userOptional = appUserRepository.findByResetPasswordCode(code);

		if (userOptional.isPresent()) return userOptional.get();

		throw new EntityNotFoundException("Reset password code không hợp lệ!");
	}

	@Override
	public AppUser create(AppUser entity){
		Optional<AppUser> check = appUserRepository.findByEmail(entity.getEmail());

		if (check.isPresent()) {
			throw new EntityExistsException("Email đã tồn tại!");
		}

		entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
		entity.setIsEnabled(false);

		return appUserRepository.save(entity);

	}

	@Override
	public List<AppUser> findAll() {
		return appUserRepository.findAll();
	}

	@Override
	public Page<AppUser> findAll(Pageable pageable) {
		return appUserRepository.findAll(pageable);
	}

	@Override
	public AppUser findById(UUID id) {
		
		Optional<AppUser> userOptional = appUserRepository.findById(id);
		
		if (userOptional.isPresent()) return userOptional.get();
		
		throw new EntityNotFoundException("User không tồn tại!");
		
	}
	
	@Override
	public boolean verify(String verificationCode) {
		
		Optional<AppUser> userOpt = appUserRepository.findByVerificationCode(verificationCode);
		
		if (userOpt.isPresent()) {
			AppUser user = userOpt.get();
			
			user.setIsEnabled(true);
			user.setVerificationCode(null);
			appUserRepository.save(user);

			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean changePassword(String email, String password) {
		return appUserRepository.changePassword(email, new BCryptPasswordEncoder().encode(password)) == 1;
	}
	
	@Override
	public AppUser updateProfile(AppUser data) {
		
		Optional<AppUser> userOptional = appUserRepository.findByEmail(data.getEmail());

		if (userOptional.isPresent()) {
			AppUser user = userOptional.get();
			user.setFirstname(data.getFirstname());
			user.setLastname(data.getLastname());
			return appUserRepository.save(user);
		}

		throw new EntityNotFoundException("User không tồn tại!");
	}

    @Override
    public boolean updateResetPasswordCode(String email, String code) {
		return appUserRepository.updateResetPasswordCode(email, code) == 1;
    }

}
