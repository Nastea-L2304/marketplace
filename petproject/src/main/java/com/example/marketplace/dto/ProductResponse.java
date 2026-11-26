package com.example.marketplace.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Marketplace product")
public record ProductResponse(
		@Schema(description = "Product id", example = "665f1c2e8b3a4d2f9c1e7a10")
		String id,

		@Schema(description = "Product title", example = "iPhone 15")
		String title,

		@Schema(description = "Product description", example = "Smartphone, 128 GB, black")
		String description,

		@Schema(description = "Product price", example = "999.99")
		BigDecimal price) {
}
