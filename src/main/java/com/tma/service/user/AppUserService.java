package com.tma.service.user;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.tma.exception.NotFoundException;
import com.tma.model.dto.response.ResponseModelDTO;
import com.tma.model.dto.response.ResponsePageDTO;
import com.tma.model.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tma.model.entity.user.AppUser;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface AppUserService {

//	ResponseModelDTO findById(UUID id);

	ResponsePageDTO findAll(String keyword, Pageable pageable);

	ResponseModelDTO createNewUser(UserCreateDTO userCreateDTO, HttpServletRequest request) throws NotFoundException, MessagingException, IOException;

	ResponseModelDTO register(UserRegisterDTO userRegisterDTO, HttpServletRequest request) throws MessagingException, IOException, NotFoundException;

	ResponseModelDTO login(UserLoginDTO userLoginDTO);

	ResponseModelDTO logout(HttpServletRequest request);

	ResponseModelDTO refreshToken(UserRefreshTokenDTO refreshTokenDTO) throws NotFoundException;

	ResponseModelDTO findByEmail(String email) throws NotFoundException;

	ResponseModelDTO verify(String verificationCode);

	ResponseModelDTO updateProfile(String email, UserUpdateDTO userUpdateDTO) throws NotFoundException;

	ResponseModelDTO changePassword(String email, UserChangePasswordDTO userChangePasswordDTO) throws NotFoundException;

	void forgotPassword(UserForgotPasswordDTO forgotPasswordDTO) throws MessagingException, IOException, NotFoundException;

	ResponseModelDTO resetPassword(UserResetPasswordDTO userResetPasswordDTO);



}
