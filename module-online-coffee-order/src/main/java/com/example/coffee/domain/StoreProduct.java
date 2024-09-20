package com.example.coffee.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "store_products")
public class StoreProduct {
    @Id
    private String storeProductId;
    @Column
    private String storeId;
    @Column
    private String productId;
    @Column
    private String stockQuantity;
}
