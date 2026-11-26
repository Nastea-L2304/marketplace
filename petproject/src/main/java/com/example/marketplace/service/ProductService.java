package com.example.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.marketplace.dto.ProductRequest;
import com.example.marketplace.dto.ProductResponse;
import com.example.marketplace.exception.ProductNotFoundException;
import com.example.marketplace.mapper.ProductMapper;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public List<ProductResponse> findAllProducts() {
		List<Product> products = productRepository.findAll();
		log.debug("Found {} products", products.size());
		return productMapper.toProductResponseList(products);
	}

	public ProductResponse findProductById(String id) {
		Product product = getProduct(id);
		log.debug("Found product: id={}", id);
		return productMapper.toProductResponse(product);
	}

	public ProductResponse createProduct(ProductRequest productRequest) {
		Product savedProduct = productRepository.save(productMapper.toProduct(productRequest));
		log.info("Product created: id={}, title={}", savedProduct.getId(), savedProduct.getTitle());
		return productMapper.toProductResponse(savedProduct);
	}

	public ProductResponse updateProduct(String id, ProductRequest productRequest) {
		Product product = getProduct(id);
		productMapper.updateProduct(productRequest, product);
		Product savedProduct = productRepository.save(product);
		log.info("Product updated: id={}, title={}", savedProduct.getId(), savedProduct.getTitle());
		return productMapper.toProductResponse(savedProduct);
	}

	public void deleteProduct(String id) {
		productRepository.delete(getProduct(id));
		log.info("Product deleted: id={}", id);
	}

	private Product getProduct(String id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}
}
