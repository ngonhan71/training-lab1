package com.tma.service.impl;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tma.entity.AppUser;
import com.tma.entity.SessionUser;
import com.tma.repository.AppUserRepository;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired(required = true)
	AppUserRepository appUserRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<AppUser> user = appUserRepository.findByEmail(username);
		
        if (user.isPresent()) {
        	return new SessionUser(user.get());
            
        }
        throw new UsernameNotFoundException(username);
	}

}
