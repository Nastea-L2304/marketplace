package com.example.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.marketplace.dto.ProductRequest;
import com.example.marketplace.dto.ProductResponse;
import com.example.marketplace.exception.ProductNotFoundException;
import com.example.marketplace.mapper.ProductMapper;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Spy
	private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

	@InjectMocks
	private ProductService productService;

	private Product product(String id) {
		return Product.builder()
				.id(id)
				.title("Phone")
				.description("Smartphone")
				.price(new BigDecimal("199.99"))
				.build();
	}

	@Test
	void findAllReturnsProducts() {
		when(productRepository.findAll()).thenReturn(List.of(product("1"), product("2")));

		List<ProductResponse> result = productService.findAllProducts();

		assertThat(result).extracting(ProductResponse::id).containsExactly("1", "2");
	}

	@Test
	void findByIdReturnsProduct() {
		when(productRepository.findById("1")).thenReturn(Optional.of(product("1")));

		ProductResponse result = productService.findProductById("1");

		assertThat(result.id()).isEqualTo("1");
		assertThat(result.title()).isEqualTo("Phone");
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(productRepository.findById("x")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.findProductById("x")).isInstanceOf(ProductNotFoundException.class);
	}

	@Test
	void createSavesProduct() {
		when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
			Product p = inv.getArgument(0);
			p.setId("1");
			return p;
		});

		ProductResponse result = productService.createProduct(new ProductRequest("Phone", "Smartphone", new BigDecimal("199.99")));

		ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
		verify(productRepository).save(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("Phone");

		assertThat(result.id()).isEqualTo("1");
		assertThat(result.title()).isEqualTo("Phone");
		assertThat(result.description()).isEqualTo("Smartphone");
		assertThat(result.price()).isEqualByComparingTo("199.99");
	}

	@Test
	void updateChangesFields() {
		when(productRepository.findById("1")).thenReturn(Optional.of(product("1")));
		when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

		ProductResponse result = productService.updateProduct("1", new ProductRequest("Laptop", "Gaming", new BigDecimal("999")));

		assertThat(result.id()).isEqualTo("1");
		assertThat(result.title()).isEqualTo("Laptop");
		assertThat(result.description()).isEqualTo("Gaming");
		assertThat(result.price()).isEqualByComparingTo("999");
	}

	@Test
	void updateThrowsWhenMissing() {
		when(productRepository.findById("x")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.updateProduct("x", new ProductRequest("a", "b", BigDecimal.ONE)))
				.isInstanceOf(ProductNotFoundException.class);
		verify(productRepository, never()).save(any());
	}

	@Test
	void deleteRemovesProduct() {
		Product p = product("1");
		when(productRepository.findById("1")).thenReturn(Optional.of(p));

		productService.deleteProduct("1");

		verify(productRepository).delete(p);
	}

	@Test
	void deleteThrowsWhenMissing() {
		when(productRepository.findById("x")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.deleteProduct("x")).isInstanceOf(ProductNotFoundException.class);
		verify(productRepository, never()).delete(any());
	}
}
