package com.example.marketplace.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.marketplace.dto.ProductRequest;
import com.example.marketplace.dto.ProductResponse;
import com.example.marketplace.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	@Mapping(target = "id", ignore = true)
	Product toProduct(ProductRequest productRequest);

	ProductResponse toProductResponse(Product product);

	List<ProductResponse> toProductResponseList(List<Product> products);

	@Mapping(target = "id", ignore = true)
	void updateProduct(ProductRequest productRequest, @MappingTarget Product product);
}
