package com.example.marketplace.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.marketplace.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class GlobalExceptionHandler {

	private static final String CODE_PREFIX = "marketplace.";

	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleProductNotFoundException(final ProductNotFoundException ex,
			final HttpServletRequest request) {
		log.warn("Product not found - Method: {}, URL: {}\n Error: {}",
				request.getMethod(),
				request.getRequestURI(),
				ex.getMessage());
		return ErrorResponse.builder()
				.code(CODE_PREFIX + "PRODUCT_NOT_FOUND")
				.message(ex.getMessage())
				.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
			final HttpServletRequest request) {
		final Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
		}
		log.warn("Validation error - Method: {}, URL: {}\n Errors: {}",
				request.getMethod(),
				request.getRequestURI(),
				errors);
		return ErrorResponse.builder()
				.code(CODE_PREFIX + "VALIDATION_FAILED")
				.message("Request validation failed")
				.errors(errors)
				.build();
	}
}
