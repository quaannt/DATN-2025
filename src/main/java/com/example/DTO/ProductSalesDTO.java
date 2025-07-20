package com.example.DTO;

import com.example.Entity.Product;

public interface ProductSalesDTO {
    Product getProduct();
    Long getTotalSold();
}