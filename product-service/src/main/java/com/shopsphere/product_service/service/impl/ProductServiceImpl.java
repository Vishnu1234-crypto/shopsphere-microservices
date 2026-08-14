package com.shopsphere.product_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopsphere.product_service.dto.ProductRequest;
import com.shopsphere.product_service.dto.ProductResponse;
import com.shopsphere.product_service.entity.Product;
import com.shopsphere.product_service.exception.ResourceNotFoundException;
import com.shopsphere.product_service.repository.ProductRepository;
import com.shopsphere.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(
            ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        Product savedProduct =
                productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(
            Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updatedProduct =
                productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public String deleteProduct(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        productRepository.delete(product);

        return "Product deleted successfully";
    }

    private ProductResponse mapToResponse(
            Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}