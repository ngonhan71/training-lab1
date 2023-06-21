package com.tma.utils;

import com.tma.model.entity.user.AppUser;
import com.tma.model.entity.user.SessionUser;
import com.tma.repository.AppUserRepository;
import com.tma.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        Optional<AppUser> userOptional = appUserRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            String roleName = roleRepository.findById(user.getRole().getId()).get().getName();
            return new SessionUser(user, roleName);

        }
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
