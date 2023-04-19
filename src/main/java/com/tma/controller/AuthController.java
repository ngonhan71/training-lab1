package com.tma.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tma.dto.user.UserDTO;
import com.tma.dto.user.UserLoginDTO;
import com.tma.dto.user.UserRegisterDTO;
import com.tma.entity.AppUser;
import com.tma.entity.Role;
import com.tma.entity.SessionUser;
import com.tma.factory.email.SendEmailService;
import com.tma.response.ResponseModel;
import com.tma.service.IAppUserService;
import com.tma.service.IRoleService;
import com.tma.utils.JwtUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authManager;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	IAppUserService appUserService;
	
	@Autowired
	IRoleService roleService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	SendEmailService sendEmailService;

	@Autowired
	RedisTemplate redisTemplate;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(HttpServletRequest request, @Valid @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException, IOException {
		
		AppUser entity = modelMapper.map(userRegisterDTO, AppUser.class);
		
		Role role = roleService.findByRoleName("CUSTOMER");
		entity.setRole(role);

		String randomCode = RandomString.make(64);
		entity.setVerificationCode(randomCode);

		String verifyURL = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/api/v1/users/verify?code=" + randomCode;

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", entity.getLastname());
		properties.put("firstname", entity.getFirstname());
		properties.put("email", entity.getEmail());
		properties.put("link", verifyURL);

		UserDTO userDTO = modelMapper.map(appUserService.create(entity), UserDTO.class);

		sendEmailService.sendVerifyAccountRegister(properties, entity.getEmail());

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", userDTO));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
			SessionUser sessionUser = (SessionUser) authentication.getPrincipal();
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("email", sessionUser.getUsername());
			
			String accessToken = jwtUtil.generateToken(map, sessionUser.getUsername());
			UserDTO userDTO = modelMapper.map(sessionUser.getUser(), UserDTO.class);
			
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("accessToken", accessToken);
			response.put("user", userDTO);

			redisTemplate.opsForValue().set(accessToken, "true", 24, TimeUnit.HOURS);

			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<HashMap<String, Object>>(true, "Successfully", response));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ResponseModel<String>(false,  e.getMessage(), null));
		}
		
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {

		String authHeader = httpServletRequest.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			String jwtToken = authHeader.substring(7);

			redisTemplate.delete(jwtToken);

			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Successfully", null));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseModel<>(true, "Failed", null));

	}
	
}
