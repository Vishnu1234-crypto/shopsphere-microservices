package com.shopsphere.product_service.service;

import java.util.List;

import com.shopsphere.product_service.dto.ProductRequest;
import com.shopsphere.product_service.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(
            ProductRequest request
    );

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(
            Long id
    );

    ProductResponse updateProduct(
            Long id,
            ProductRequest request
    );

    String deleteProduct(
            Long id
    );
}