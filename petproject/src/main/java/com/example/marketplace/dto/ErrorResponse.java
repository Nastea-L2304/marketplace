package com.example.marketplace.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response")
public record ErrorResponse(
		@Schema(description = "Error code", example = "marketplace.PRODUCT_NOT_FOUND")
		String code,

		@Schema(description = "Error message", example = "Product not found")
		String message,

		@Schema(description = "Validation errors by field", example = "{\"title\": \"must not be blank\"}")
		Map<String, String> errors) {
}
