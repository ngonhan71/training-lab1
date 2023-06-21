package com.tma.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import com.tma.model.dto.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tma.model.entity.user.SessionUser;
import com.tma.service.user.AppUserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	@Autowired
	private AppUserService appUserService;

	@GetMapping
	@Operation(summary = "Get all users")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<ResponsePageDTO> getAll(
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page, //page number
			@RequestParam(name = "limit", defaultValue = "20") int limit, //page size
			@RequestParam(name = "orderBy", defaultValue = "email") String orderBy, //database field
			@RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
	) {

		Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);

		Pageable pageable = PageRequest.of(page, limit, sort);
		return ResponseEntity.ok().body(appUserService.findAll(keyword, pageable));
	}

	@PostMapping
	@Operation(summary = "Create a new user")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<ResponseModelDTO> create(HttpServletRequest request, @Valid @RequestBody UserCreateDTO userCreateDTO)
			throws MessagingException, IOException, NotFoundException {
		return ResponseEntity.ok().body(appUserService.createNewUser(userCreateDTO, request));
	}

	@GetMapping("/verify")
	@Operation(summary = "Verify email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Verify email successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "400", description = "Code is invalid")

	})
	public ResponseEntity<?> verifyUser(@RequestParam String code) {
		return ResponseEntity.ok().body(appUserService.verify(code));
	}

	@GetMapping("/profile")
	@Operation(summary = "Get user profile")
	public ResponseEntity<ResponseModelDTO> getProfile() throws NotFoundException {
		SessionUser sessionUser = (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok().body(appUserService.findByEmail(sessionUser.getUsername()));
	}

	@PutMapping("/profile")
	@Operation(summary = "Update lastname, firstname user")
	public ResponseEntity<?> updateProfile(@RequestBody UserUpdateDTO userUpdateDTO) throws NotFoundException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok().body(appUserService.updateProfile(userDetails.getUsername(), userUpdateDTO));
	}

	@PutMapping("/change-password")
	@Operation(summary = "Change password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDTO userChangePasswordDTO) throws NotFoundException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return ResponseEntity.ok().body(appUserService.changePassword(userDetails.getUsername(), userChangePasswordDTO));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody UserForgotPasswordDTO userForgotPasswordDTO)
			throws MessagingException, IOException, NotFoundException {

		appUserService.forgotPassword(userForgotPasswordDTO);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree("{\"message\":\"Send email reset password successfully\"}");

		return ResponseEntity.ok().body(json);

	}

	@PutMapping("/reset-password")
	@Operation(summary = "Reset password")
	public ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordDTO userResetPasswordDTO) {

		return ResponseEntity.ok().body(appUserService.resetPassword(userResetPasswordDTO));

	}

}
