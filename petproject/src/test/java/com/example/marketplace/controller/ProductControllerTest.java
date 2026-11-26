package com.example.marketplace.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.marketplace.dto.ProductRequest;
import com.example.marketplace.exception.ProductNotFoundException;
import com.example.marketplace.dto.ProductResponse;
import com.example.marketplace.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	private static final String VALID_BODY = """
			{"title":"Phone","description":"Smartphone","price":199.99}""";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	private ProductResponse product(String id) {
		return new ProductResponse(id, "Phone", "Smartphone", new BigDecimal("199.99"));
	}

	@Test
	void getAllReturnsList() throws Exception {
		when(productService.findAllProducts()).thenReturn(List.of(product("1"), product("2")));

		mockMvc.perform(get("/api/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value("1"));
	}

	@Test
	void getByIdReturnsProduct() throws Exception {
		when(productService.findProductById("1")).thenReturn(product("1"));

		mockMvc.perform(get("/api/products/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Phone"))
				.andExpect(jsonPath("$.price").value(199.99));
	}

	@Test
	void getByIdReturns404WhenMissing() throws Exception {
		when(productService.findProductById("x")).thenThrow(new ProductNotFoundException("x"));

		mockMvc.perform(get("/api/products/x"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("marketplace.PRODUCT_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Product not found: x"));
	}

	@Test
	void createReturns201() throws Exception {
		when(productService.createProduct(any(ProductRequest.class))).thenReturn(product("1"));

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value("1"));
	}

	@Test
	void createReturns400WhenInvalid() throws Exception {
		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title":"","price":-1}"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("marketplace.VALIDATION_FAILED"))
				.andExpect(jsonPath("$.errors.title").exists())
				.andExpect(jsonPath("$.errors.price").exists());
	}

	@Test
	void updateReturnsProduct() throws Exception {
		when(productService.updateProduct(eq("1"), any(ProductRequest.class))).thenReturn(product("1"));

		mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("1"));
	}

	@Test
	void updateReturns404WhenMissing() throws Exception {
		when(productService.updateProduct(eq("x"), any(ProductRequest.class))).thenThrow(new ProductNotFoundException("x"));

		mockMvc.perform(put("/api/products/x").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteReturns204() throws Exception {
		mockMvc.perform(delete("/api/products/1")).andExpect(status().isNoContent());

		verify(productService).deleteProduct("1");
	}

	@Test
	void deleteReturns404WhenMissing() throws Exception {
		doThrow(new ProductNotFoundException("x")).when(productService).deleteProduct("x");

		mockMvc.perform(delete("/api/products/x")).andExpect(status().isNotFound());
	}
}
