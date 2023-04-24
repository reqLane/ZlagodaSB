package com.example.zlagodasb.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductModel {
    private Integer categoryNumber;
    private String productName;
    private String manufacturer;
    private String characteristics;

    public Product toEntity() {
        Product entity = new Product();
        entity.setCategoryNumber(categoryNumber);
        entity.setProductName(productName);
        entity.setManufacturer(manufacturer);
        entity.setCharacteristics(characteristics);
        return entity;
    }
}
