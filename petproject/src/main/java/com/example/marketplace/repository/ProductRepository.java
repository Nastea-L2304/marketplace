package com.example.marketplace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.marketplace.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
