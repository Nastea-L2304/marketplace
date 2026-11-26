package com.example.marketplace.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data for creating or updating a product")
public record ProductRequest(
		@Schema(description = "Product title", example = "iPhone 15", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotBlank String title,

		@Schema(description = "Product description", example = "Smartphone, 128 GB, black")
		String description,

		@Schema(description = "Product price", example = "999.99", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull @DecimalMin("0.0") BigDecimal price) {
}
