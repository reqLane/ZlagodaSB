package com.example.zlagodasb.product.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductInfo {
    private Integer idProduct;
    private String categoryName;
    private String productName;
    private String manufacturer;
    private String characteristics;
}
