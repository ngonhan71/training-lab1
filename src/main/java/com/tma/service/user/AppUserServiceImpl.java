package com.tma.service.user;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.tma.enums.DefaultRole;
import com.tma.enums.TokenTypes;
import com.tma.exception.BadRequestException;
import com.tma.exception.NotFoundException;
import com.tma.factory.email.SendEmailService;
import com.tma.mapper.UserMapper;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import com.tma.model.dto.user.*;
import com.tma.model.entity.role.Role;
import com.tma.model.entity.user.SessionUser;
import com.tma.repository.RoleRepository;
import com.tma.utils.JwtUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tma.model.entity.user.AppUser;
import com.tma.repository.AppUserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AppUserServiceImpl implements AppUserService {

	private String rootUrl = "http://localhost:8080/";

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SendEmailService sendEmailService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public ResponsePageDTO findAll(String keyword, Pageable pageable) {

		Page<AppUser> userPage = appUserRepository.findAll(keyword, pageable);

		List<UserBasicDTO> userBasicDTOS = new ArrayList<>();
		for(AppUser appUser : userPage.getContent()) {
			userBasicDTOS.add(userMapper.fromEntityToBasic(appUser));
		}

		return ResponsePageDTO.builder()
				.data(userBasicDTOS)
				.limit(userPage.getSize())
				.currentPage(userPage.getNumber())
				.totalItems(userPage.getTotalElements())
				.totalPages(userPage.getTotalPages())
				.build();
	}

	@Override
	public ResponseModelDTO findByEmail(String email) throws NotFoundException {

		AppUser user = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("User not found: " + email));

		return ResponseModelDTO.builder()
				.data(userMapper.fromEntityToBasic(user))
				.isSuccess(true)
				.build();
	}

	@Override
	@Transactional
	public ResponseModelDTO createNewUser(UserCreateDTO userCreateDTO, HttpServletRequest request) throws NotFoundException, MessagingException, IOException {
		Role role = roleRepository.findById(userCreateDTO.getRoleId())
				.orElseThrow(() -> new NotFoundException("Not found role with id: " + userCreateDTO.getRoleId()));

		if (appUserRepository.existsByEmail(userCreateDTO.getEmail())) {
			throw new BadRequestException("User with this email already exists");
		}

		AppUser appUser = userMapper.fromCreateToEntity(userCreateDTO);

		String randomCode = RandomString.make(64);
		String randomPassword = RandomString.make(8);

		appUser.setVerificationCode(randomCode);
		appUser.setRole(role);
		appUser.setPassword(passwordEncoder.encode(randomPassword));

		String verifyURL = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/api/v1/users/verify?code=" + randomCode;

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", appUser.getLastname());
		properties.put("firstname", appUser.getFirstname());
		properties.put("email", appUser.getEmail());
		properties.put("password", randomPassword);
		properties.put("link", verifyURL);

		UserBasicDTO userBasicDTO = userMapper.fromEntityToBasic(appUserRepository.save(appUser));

		sendEmailService.sendVerifyAccountCreate(properties, appUser.getEmail());

		return ResponseModelDTO.builder()
				.data(userBasicDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	@Transactional
	public ResponseModelDTO register(UserRegisterDTO userRegisterDTO, HttpServletRequest request) throws MessagingException, IOException, NotFoundException {

		Role role = roleRepository.findByName(DefaultRole.CUSTOMER.label)
				.orElseThrow(() -> new NotFoundException("Not found role with name: " + DefaultRole.CUSTOMER.label));

		if (appUserRepository.existsByEmail(userRegisterDTO.getEmail())) {
			throw new BadRequestException("User with this email already exists");
		}

		AppUser appUser = userMapper.fromRegisterToEntity(userRegisterDTO);

		String randomCode = RandomString.make(64);
		appUser.setVerificationCode(randomCode);
		appUser.setRole(role);
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

		String verifyURL = request.getRequestURL().toString().replace(request.getServletPath(), "") + "/api/v1/users/verify?code=" + randomCode;

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", appUser.getLastname());
		properties.put("firstname", appUser.getFirstname());
		properties.put("email", appUser.getEmail());
		properties.put("link", verifyURL);

		UserBasicDTO userBasicDTO = userMapper.fromEntityToBasic(appUserRepository.save(appUser));

		sendEmailService.sendVerifyAccountRegister(properties, appUser.getEmail());

		return ResponseModelDTO.builder()
				.data(userBasicDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO login(UserLoginDTO userLoginDTO) {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			SessionUser sessionUser = (SessionUser) authentication.getPrincipal();

			String accessToken = jwtUtil.generateToken(sessionUser.getUser(), TokenTypes.ACCESS_TOKEN);
			String refreshToken = jwtUtil.generateToken(sessionUser.getUser(), TokenTypes.REFRESH_TOKEN);

			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("accessToken", accessToken);
			response.put("refreshToken", refreshToken);
			response.put("user", userMapper.fromEntityToBasic(sessionUser.getUser()));

			return ResponseModelDTO.builder()
					.data(response)
					.isSuccess(true)
					.build();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new BadRequestException("Incorrect username or password");
		}
	}

	@Override
	public ResponseModelDTO logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			jwtUtil.blacklistToken(authHeader.substring(7));
			return ResponseModelDTO.builder()
					.data("Logout successfully")
					.isSuccess(true)
					.build();
		}
		throw new BadRequestException("Incorrect username or password");
	}

	@Override
	public ResponseModelDTO refreshToken(UserRefreshTokenDTO refreshTokenDTO) throws NotFoundException {
		String refreshToken = refreshTokenDTO.getRefreshToken();

		if (redisTemplate.hasKey("revoked_jwt:" + refreshToken)) {
			throw new BadRequestException("refreshToken is invalid");
		}

		String username = jwtUtil.getUsername(refreshToken);
		AppUser user = appUserRepository.findByEmail(username)
				.orElseThrow(() -> new NotFoundException("User not found: " + username));

		String newAccessToken = jwtUtil.generateToken(user, TokenTypes.ACCESS_TOKEN);
		String newRefreshToken = jwtUtil.generateToken(user, TokenTypes.REFRESH_TOKEN);

		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("accessToken", newAccessToken);
		response.put("refreshToken", newRefreshToken);

		jwtUtil.blacklistToken(refreshToken);

		return ResponseModelDTO.builder()
				.data(response)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO verify(String verificationCode) {

		AppUser user = appUserRepository.findByVerificationCode(verificationCode)
				.orElseThrow(() -> new BadRequestException("Code is invalid"));

		user.setEnabled(true);
		user.setVerificationCode(null);

		return ResponseModelDTO.builder()
				.data(userMapper.fromEntityToBasic(appUserRepository.save(user)))
				.isSuccess(true)
				.build();

	}
	
	@Override
	public ResponseModelDTO changePassword(String email, UserChangePasswordDTO userChangePasswordDTO) throws NotFoundException {
		AppUser user = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("User not found: " + email));

		if (passwordEncoder.matches(userChangePasswordDTO.getCurrentPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));

			return ResponseModelDTO.builder()
					.data(userMapper.fromEntityToBasic(appUserRepository.save(user)))
					.isSuccess(true)
					.build();

		} else throw new BadRequestException("Current password is invalid");
	}

	@Override
	@Transactional
	public void forgotPassword(UserForgotPasswordDTO forgotPasswordDTO) throws MessagingException, IOException, NotFoundException {
		String email = forgotPasswordDTO.getEmail();

		AppUser user = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("User not found: " + email));

		String randomCode = RandomString.make(64);

		user.setResetPasswordCode(randomCode);
		appUserRepository.save(user);

		String link = rootUrl + "/swagger-ui/index.html#/user-controller/resetPassword?code=" + randomCode;
		Map<String, String> properties = new HashMap<String, String>();

		properties.put("lastname", user.getLastname());
		properties.put("firstname", user.getFirstname());
		properties.put("link", link);

		sendEmailService.sendForgotPasswordEmail(properties, user.getEmail());

	}

	@Override
	public ResponseModelDTO resetPassword(UserResetPasswordDTO userResetPasswordDTO) {
		AppUser user = appUserRepository.findByResetPasswordCode(userResetPasswordDTO.getResetPasswordCode())
				.orElseThrow(() -> new BadRequestException("Code is invalid"));

		user.setPassword(passwordEncoder.encode(userResetPasswordDTO.getPassword()));
		user.setResetPasswordCode(null);

		return ResponseModelDTO.builder()
				.data(userMapper.fromEntityToBasic(appUserRepository.save(user)))
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO updateProfile(String email, UserUpdateDTO userUpdateDTO) throws NotFoundException {

		AppUser user = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("User not found: " + email));

		user.setFirstname(userUpdateDTO.getFirstname());
		user.setLastname(userUpdateDTO.getLastname());

		return ResponseModelDTO.builder()
				.data(userMapper.fromEntityToBasic(appUserRepository.save(user)))
				.isSuccess(true)
				.build();
	}

}
