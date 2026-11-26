package com.example.marketplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketplace.dto.ErrorResponse;
import com.example.marketplace.dto.ProductRequest;
import com.example.marketplace.dto.ProductResponse;
import com.example.marketplace.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalog management for the marketplace")
public class ProductController {

	private final ProductService productService;

	@GetMapping
	@Operation(summary = "Get all products")
	@ApiResponse(responseCode = "200", description = "List of products",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
	public List<ProductResponse> getAllProducts() {
		return productService.findAllProducts();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by id")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Product found",
					content = @Content(schema = @Schema(implementation = ProductResponse.class))),
			@ApiResponse(responseCode = "404", description = "Product not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ProductResponse getProductById(@Parameter(description = "Product id", example = "665f1c2e8b3a4d2f9c1e7a10")
			@PathVariable String id) {
		return productService.findProductById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create product")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Product created",
					content = @Content(schema = @Schema(implementation = ProductResponse.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ProductResponse createProduct(@Valid @RequestBody ProductRequest productRequest) {
		return productService.createProduct(productRequest);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update product")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Product updated",
					content = @Content(schema = @Schema(implementation = ProductResponse.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "Product not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public ProductResponse updateProduct(@Parameter(description = "Product id", example = "665f1c2e8b3a4d2f9c1e7a10")
			@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
		return productService.updateProduct(id, productRequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete product")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Product deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Product not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	public void deleteProduct(@Parameter(description = "Product id", example = "665f1c2e8b3a4d2f9c1e7a10")
			@PathVariable String id) {
		productService.deleteProduct(id);
	}
}
