package com.tma.exception;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.tma.response.ResponseModel;

@RestControllerAdvice
public class CustomExceptionHandler {

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleAgrumentNotValidException(final MethodArgumentNotValidException ex) {
		
		List<String> errs =  new ArrayList<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
				 String fieldName = ((FieldError) error).getField();
				 String errorMessage = error.getDefaultMessage();
				 errs.add(fieldName + " : "  + errorMessage);
		});

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        		new ResponseModel<>(false, errs.toString()));
    }
	
	@ExceptionHandler(EntityExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleEntityExistsException(EntityExistsException ex, WebRequest req) {
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	        		new ResponseModel<>(false, ex.getMessage()));
		 
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest req) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        		new ResponseModel<>(false, ex.getMessage()));
	}
		
}
