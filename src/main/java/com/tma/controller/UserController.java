package com.tma.controller;

import java.io.IOException;
import java.util.*;

import com.tma.dto.user.*;
import com.tma.entity.Role;
import com.tma.factory.email.SendEmailService;
import com.tma.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tma.entity.AppUser;
import com.tma.entity.SessionUser;
import com.tma.response.ResponseModel;
import com.tma.service.IAppUserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	IAppUserService appUserService;

	@Autowired
	IRoleService roleService;
	
	@Autowired
	SendEmailService sendEmailService;
	
	@Autowired
	AuthenticationManager authManager;

	@GetMapping("")
	@Operation(summary = "Get all users")
	@PreAuthorize("hasAuthority('USER_VIEWALL')")
	public ResponseEntity<?> getAll(
			@RequestParam(defaultValue = "1") @Positive int page,
			@RequestParam(defaultValue = "5") @Positive int limit
	) {

		Pageable paging = PageRequest.of(page - 1, limit, Sort.by("email").ascending());

		List<UserDTO> users = new ArrayList<>();
		Page<AppUser> pageAppUsers = appUserService.findAll(paging);

		for(AppUser u : pageAppUsers.getContent()) {
			users.add(modelMapper.map(u, UserDTO.class));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("users", users);
		response.put("currentPage", pageAppUsers.getNumber() + 1);
		response.put("totalItems", pageAppUsers.getTotalElements());
		response.put("totalPages", pageAppUsers.getTotalPages());

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", response));
	}

	@PostMapping("")
	@Operation(summary = "Create a new user")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	public ResponseEntity<?> create(HttpServletRequest request, @Valid @RequestBody UserCreateDTO userCreateDTO)
			throws MessagingException, IOException {

		AppUser entity = modelMapper.map(userCreateDTO, AppUser.class);

		Role role = roleService.findById(userCreateDTO.getRoleId());
		entity.setRole(role);

		String randomCode = RandomString.make(64);
		entity.setVerificationCode(randomCode);

		String randomPassword = RandomString.make(8);
		entity.setPassword(randomPassword);

		String verifyURL = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/api/v1/users/verify?code=" + randomCode;

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", entity.getLastname());
		properties.put("firstname", entity.getFirstname());
		properties.put("email", entity.getEmail());
		properties.put("password", randomPassword);
		properties.put("link", verifyURL);

		UserDTO userDTO = modelMapper.map(appUserService.create(entity), UserDTO.class);

		sendEmailService.sendVerifyAccountCreate(properties, entity.getEmail());

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", userDTO));
	}

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam String code) {
		if (appUserService.verify(code)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<>(true, "Verify successfully", null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(false, "Verify failed", null));
	}
	
	@GetMapping("/profile")
	@Operation(summary = "Get user profile")
	public ResponseEntity<?> getProfile() {
		SessionUser sessionUser = (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDTO data = modelMapper.map((AppUser)sessionUser.getUser(), UserDTO.class);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", data));
	}
	
	@PutMapping("/profile")
	@Operation(summary = "Update lastname, firstname user")
	public ResponseEntity<?> updateProfile(@RequestBody UserUpdateDTO userUpdateDTO) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		AppUser appUser = modelMapper.map(userUpdateDTO, AppUser.class);
		
		appUser.setEmail(userDetails.getUsername());
		
		UserDTO userDTO = modelMapper.map(appUserService.updateProfile(appUser), UserDTO.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Update profile successfully", userDTO));
	}
	
	@PutMapping("/change-password")
	@Operation(summary = "Change password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			authManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userChangePasswordDTO.getCurrentPassword()));

			if (appUserService.changePassword(userDetails.getUsername(), userChangePasswordDTO.getNewPassword())) {
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseModel<HashMap<String, Object>>(true, "Change password Successfully", null));
			}
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseModel<HashMap<String, Object>>(false, "Failed", null));
			
		} catch (BadCredentialsException badCredentialsException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseModel<String>(false, "Current password is invalid", null));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ResponseModel<String>(false, ex.getMessage(), null));
		}
		
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(HttpServletRequest request, @RequestBody UserForgotPasswordDTO userForgotPasswordDTO)
			throws MessagingException, IOException {

		AppUser user = appUserService.findByEmail(userForgotPasswordDTO.getEmail());

		String randomCode = RandomString.make(64);
		appUserService.updateResetPasswordCode(userForgotPasswordDTO.getEmail(), randomCode);

		String rootUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
		String link = rootUrl + "/swagger-ui/index.html#/user-controller/resetPassword?code=" + randomCode;

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", user.getLastname());
		properties.put("firstname", user.getFirstname());
		properties.put("link", link);

		sendEmailService.sendForgotPasswordEmail(properties, user.getEmail());

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseModel<>(true, "Successfully", null));
	}

	@PutMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordDTO userResetPasswordDTO) {

		AppUser user = appUserService.findByResetPasswordCode(userResetPasswordDTO.getResetPasswordCode());

		if (appUserService.changePassword(user.getEmail(), userResetPasswordDTO.getPassword())) {
			appUserService.updateResetPasswordCode(user.getEmail(), "");
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseModel<HashMap<String, Object>>(true, "Reset password Successfully", null));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ResponseModel<HashMap<String, Object>>(false, "Failed", null));

	}

}
