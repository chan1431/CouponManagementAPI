package com.couponmanagement.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {
	
	@ExceptionHandler(CouponNotFoundException.class)
	public ResponseEntity<String> CouponNotFoundExceptionHandler(CouponNotFoundException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(TypeMissMatchException.class)
	public ResponseEntity<String> TypeMissMatchExceptionHandler(TypeMissMatchException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	@ExceptionHandler(NoGetProductsException.class)
	public ResponseEntity<String> NoGetProductsExceptionHandler(NoGetProductsException e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

}
