package com.tma.exception;

import java.net.BindException;
import java.util.Date;

import javax.persistence.EntityExistsException;

import com.tma.enums.StatusMessage;
import com.tma.model.dto.response.ErrorDTO;
import com.tma.model.dto.response.ResponseModelDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler({
			BadRequestException.class,
			BindException.class,
			MethodArgumentNotValidException.class,
			MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class
	})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handlerRequestException(Exception ex) {
		return ErrorDTO.builder()
						.code(StatusMessage.BAD_REQUEST.label)
						.status(StatusMessage.BAD_REQUEST)
						.timestamp(new Date())
						.message(ex.getLocalizedMessage())
						.build();

	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseModelDTO handleNotFoundException(NotFoundException ex) {
		return ResponseModelDTO.builder()
				.error(ErrorDTO.builder()
						.code(StatusMessage.NOT_FOUND.label)
						.status(StatusMessage.NOT_FOUND)
						.timestamp(new Date())
						.message(ex.getLocalizedMessage())
						.build())
				.build();

	}
	
	@ExceptionHandler({AuthenticationException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorDTO unauthorizedException(AuthenticationException ex) {
		return ErrorDTO.builder()
				.code(StatusMessage.UNAUTHORIZED.label)
				.status(StatusMessage.UNAUTHORIZED)
				.timestamp(new Date())
				.message(ex.getLocalizedMessage())
				.build();
	}

	@ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorDTO forbiddenException(AuthenticationException ex) {
		return ErrorDTO.builder()
				.code(StatusMessage.FORBIDDEN.label)
				.status(StatusMessage.FORBIDDEN)
				.timestamp(new Date())
				.message(ex.getLocalizedMessage())
				.build();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseError handleAllException(Exception ex, WebRequest webRequest) {
		return new ResponseError(10000, ex.getLocalizedMessage());
	}

}
