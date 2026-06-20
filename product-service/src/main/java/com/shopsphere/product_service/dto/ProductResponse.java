package com.shopsphere.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;
}